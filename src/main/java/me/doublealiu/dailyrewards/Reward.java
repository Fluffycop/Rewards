package me.doublealiu.dailyrewards;

import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

class Reward {
    private Set<ItemStack> rewardItems;
    private String name;
    private int refreshHours;

    public Reward(){

    }

    //Getters and setteers
    public Set<ItemStack> getRewardItems() {
        return rewardItems;
    }

    public void setRewardItems(Set<ItemStack> rewardItems) {
        this.rewardItems = rewardItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefreshHours() {
        return refreshHours;
    }

    public void setRefreshHours(int refreshHours) {
        this.refreshHours = refreshHours;
    }
}
