package me.doublealiu.dailyrewards;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class DailyRewards extends JavaPlugin {
    protected final YMLConfig config = new YMLConfig(this);

    public static final long MINUTE_TICKS = 1200;

    public static final boolean vaultEnabled = Bukkit.getServer().getPluginManager().getPlugin("Vault") != null;
    public static boolean ecoEnabled;

    public static Economy economy;

    @Override
    public void onEnable(){
        enableCheck();
        config.loadYamls();
        setupEconomy();

        BukkitTask task = new RewardDecrementerTask().runTaskTimerAsynchronously(this, MINUTE_TICKS, MINUTE_TICKS);
    }

    @Override
    public void onDisable(){
        config.saveYamls();
    }

    private boolean enableCheck(){
        if(!config.isEnabled()){
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> ecoProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (ecoProvider != null && vaultEnabled) {
            economy = ecoProvider.getProvider();
        }
        ecoEnabled = economy != null;
        return economy != null;
    }
}