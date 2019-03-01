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
    //TODO: Test in an actual server environment

    //const
    private final File DATA_FOLDER = new File(this.getDataFolder().getAbsolutePath() + File.separator + "data");
    private final String VAULT_NAME = "Vault";

    private YMLConfig config;
    private StateAccessor dataAccessor;
    private BukkitTask rewardDecrementerTask;

    private boolean vaultEnabled;
    private boolean ecoEnabled;

    public static Economy economy;

    @Override
    public void onEnable(){
        initStaticEntities();
        setupVault();
        enableCheck();
        config.loadYamls();
        registerListeners();
    }

    @Override
    public void onDisable(){
        rewardDecrementerTask.cancel();
        config.saveYamls();
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new FirstJoin(), this);
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
        rewardDecrementerTask = new RewardDecrementerTask(this).runTaskTimerAsynchronously(this, RewardDecrementerTask.SECOND_TICKS * 60, RewardDecrementerTask.SECOND_TICKS);
    }

    private void setupVault(){
        vaultEnabled = Bukkit.getServer().getPluginManager().getPlugin(VAULT_NAME) != null;
        if(!vaultEnabled){
            return;
        }

        RegisteredServiceProvider<Economy> ecoProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if(ecoProvider != null){
            economy = ecoProvider.getProvider();
        }
        ecoEnabled = economy != null;
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