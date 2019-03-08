package me.fluffycop.dailyrewards;

import me.fluffycop.dailyrewards.command.RewardCommandManager;
import me.fluffycop.dailyrewards.listener.FirstJoin;
import me.fluffycop.dailyrewards.entity.staticentity.RewardDecrementerTask;
import me.fluffycop.dailyrewards.entity.staticentity.StateAccessor;
import me.fluffycop.dailyrewards.entity.staticentity.YMLConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public class DailyRewards extends JavaPlugin {
    //TODO: Test persistent data system and change first join listener

    //const
    public final File DATA_FOLDER = new File(this.getDataFolder().getAbsolutePath() + File.separator + "data");

    private YMLConfig config;
    private StateAccessor dataAccessor;
    private BukkitTask rewardDecrementerTask;

    private boolean ecoEnabled;

    public static Economy economy;

    @Override
    public void onEnable(){
        setupVault();
        initStaticEntities();
        enableCheck();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable(){
        rewardDecrementerTask.cancel();
        dataAccessor.saveAll();
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new FirstJoin(this), this);
    }

    private void registerCommands(){
        getCommand(RewardCommandManager.REWARD_CMD_ALIAS).setExecutor(new RewardCommandManager());
    }

    private boolean enableCheck(){
        if(!config.isEnabled()){
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    private void initStaticEntities(){
        config = new YMLConfig(this);
        dataAccessor = new StateAccessor(this, DATA_FOLDER);
        dataAccessor.loadAll();
        rewardDecrementerTask = new RewardDecrementerTask().runTaskTimerAsynchronously(this, RewardDecrementerTask.SECOND_TICKS * 60, RewardDecrementerTask.SECOND_TICKS);
    }

    private void setupVault(){
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            ecoEnabled = false;
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            ecoEnabled = false;
            return;
        }
        economy = rsp.getProvider();
        ecoEnabled = economy != null;
    }

    public YMLConfig getYMLConfig() {
        return config;
    }

    public StateAccessor getDataAccessor() {
        return dataAccessor;
    }

    public boolean isEcoEnabled() {
        return ecoEnabled;
    }
}