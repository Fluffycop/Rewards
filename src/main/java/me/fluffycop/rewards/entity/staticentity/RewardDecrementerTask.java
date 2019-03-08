package me.fluffycop.dailyrewards.entity.staticentity;

import me.fluffycop.dailyrewards.DailyRewards;
import me.fluffycop.dailyrewards.entity.CustomPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.Map;

public class RewardDecrementerTask extends BukkitRunnable {

    private Date nextRun = new Date(new Date().getTime() + MINUTES_MS);

    //consts
    public static final long SECOND_TICKS = 20;
    public static final long MINUTES_MS = 60000;

    @Override
    public void run(){
        if(checkForAccuracy()){
            nextRun.setTime(nextRun.getTime() + MINUTES_MS);

            //decrement from players
            for(CustomPlayer cPlayer : CustomPlayer.get()){
                for(String reward : cPlayer.getRewardMinutesMap().keySet()) {
                    Map<String, Integer> rewardMinuteMap = cPlayer.getRewardMinutesMap();
                    int oldMinute = rewardMinuteMap.get(reward);
                    if(oldMinute != 0){
                        cPlayer.replace(reward, oldMinute - 1);
                    }
                }
            }
        }
    }

    private boolean checkForAccuracy(){
        return new Date().getTime() >= nextRun.getTime();
    }
}
