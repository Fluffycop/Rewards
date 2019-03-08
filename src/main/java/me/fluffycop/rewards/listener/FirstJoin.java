package me.fluffycop.dailyrewards.listener;

import me.fluffycop.dailyrewards.DailyRewards;
import me.fluffycop.dailyrewards.entity.CustomPlayer;
import me.fluffycop.dailyrewards.entity.Reward;
import me.fluffycop.dailyrewards.entity.staticentity.StateAccessor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;

public class FirstJoin implements Listener {

    DailyRewards plugin;

    public FirstJoin(DailyRewards plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String id = player.getUniqueId().toString();
        File dataFile = new File(plugin.DATA_FOLDER + File.separator + id + ".json");
        if(!dataFile.exists()){
            new CustomPlayer(id);
        }
    }
}
