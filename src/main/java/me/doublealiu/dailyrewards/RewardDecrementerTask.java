package me.doublealiu.dailyrewards;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RewardDecrementerTask extends BukkitRunnable {

    private Player player;
    private Map<Reward, Integer> rewardTimer;

    public final static Set<RewardDecrementerTask> playerDecrementerList = new HashSet<>();

    public RewardDecrementerTask(Player player){
        this.player = player;
    }

    //try making asynch so no api calls
    @Override
    public void run(){

    }
}
