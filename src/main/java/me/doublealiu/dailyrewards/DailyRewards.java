package me.doublealiu.dailyrewards;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DailyRewards extends JavaPlugin {
    protected final YMLConfig config = new YMLConfig(this);

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
}