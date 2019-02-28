package me.fluffycop.dailyrewards.listener;

import me.fluffycop.dailyrewards.entity.CustomPlayer;
import me.fluffycop.dailyrewards.entity.Reward;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class FirstJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String id = player.getUniqueId().toString();
        if(player.hasPlayedBefore()){
            new CustomPlayer(id);
        }
    }
}
