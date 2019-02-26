package me.fluffycop.dailyrewards;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DailyRewards extends JavaPlugin {
    //TODO: Add command manager and register the command
    //TODO: Test in an actual server environment

    //const
    public final File DATA_FOLDER = new File(this.getDataFolder().getAbsolutePath() + File.separator + "data");

    private final YMLConfig config = new YMLConfig(this);
    private final StateAccessor dataAccessor = new StateAccessor(this, DATA_FOLDER);

    private final BukkitTask rewardDecrementerTask = new RewardDecrementerTask(this).runTaskTimerAsynchronously(this, RewardDecrementerTask.SECOND_TICKS * 60, RewardDecrementerTask.SECOND_TICKS);

    private final boolean vaultEnabled = Bukkit.getServer().getPluginManager().getPlugin("Vault") != null;
    private final boolean ecoEnabled = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider() != null;

    public static Economy economy;

    @Override
    public void onEnable(){
        enableCheck();
        config.loadYamls();
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

    public YMLConfig getYMLConfig() {
        return config;
    }

    public StateAccessor getDataAccessor() {
        return dataAccessor;
    }

    public boolean isVaultEnabled() {
        return vaultEnabled;
    }

    public boolean isEcoEnabled() {
        return ecoEnabled;
    }
}