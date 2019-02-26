package me.fluffycop.dailyrewards;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.Map;

public class RewardDecrementerTask extends BukkitRunnable {

    private DailyRewards plugin;

    private Date nextRun = new Date();
    private boolean enabled;

    //consts
    public static final long SECOND_TICKS = 20;
    public static final long MINUTES_MS = 60000;

    public RewardDecrementerTask(DailyRewards plugin){
        super();
        this.plugin = plugin;
        enabled = true;
    }

    @Override
    public void run(){
        if(!checkForAccuracy() && this.enabled){
            Date cacheDate = new Date();
            cacheDate.setTime(cacheDate.getTime() + MINUTES_MS);
            nextRun = cacheDate;
            for(CustomPlayer cPlayer : CustomPlayer.get()){
                for(String reward : cPlayer.getRewardMinutesMap().keySet()) {
                    Map<String, Integer> rewardMinuteMap = cPlayer.getRewardMinutesMap();
                    int oldMinute = rewardMinuteMap.get(reward);
                    if(oldMinute != 0){
                        rewardMinuteMap.put(reward, oldMinute - 1);
                    }
                }
            }
        }
    }

    private boolean checkForAccuracy(){
        return new Date().getTime() >= nextRun.getTime();
    }

    public synchronized void stop(){
        enabled = false;
    }
}
