package me.fluffycop.dailyrewards;

import org.bukkit.entity.Player;

import java.util.*;

public class CustomPlayer {
    private String playerID;
    private final Map<String, Integer> rewardMinutesMap = new HashMap<>();

    private final static Set<CustomPlayer> customPlayerSet = Collections.synchronizedSet(new HashSet<CustomPlayer>());

    public CustomPlayer(){}

    //Used in state accessor class
    public CustomPlayer(String id){
        this.playerID = id;
        for(Reward reward : Reward.get()){
            int minutes = reward.getRefreshMinutes();
            rewardMinutesMap.put(reward.getName(), minutes);
        }
        add(this);
    }

    public static CustomPlayer getByID(String ID){
        for(CustomPlayer player : get()){
            if(player.getPlayerID().equals(ID)){
                return player;
            }
        }
        return null;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public Map<String, Integer> getRewardMinutesMap() {
        return rewardMinutesMap;
    }

    public static synchronized void add(CustomPlayer player){
        customPlayerSet.add(player);
    }

    public static synchronized Set<CustomPlayer> get(){
        return customPlayerSet;
    }
}