package me.fluffycop.dailyrewards.entity;

import me.fluffycop.dailyrewards.DailyRewards;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Reward {
    private Set<ItemStack> rewardItemsVanilla = new HashSet<>();
    private Map<CustomItem, Integer> rewardItemsCustom = new HashMap<>();
    private double money;
    private String name;
    private String permission;
    private int refreshMinutes;

    private final static Set<Reward> rewardSet = Collections.synchronizedSet(new HashSet<Reward>());

    public Reward(){
        money = 0.0;
        add(this);
    }

    //Getters and setteers
    public Set<ItemStack> getRewardItemsVanilla() {
        return rewardItemsVanilla;
    }

    public void setRewardItemsVanilla(Set<ItemStack> rewardItemsVanilla) {
        this.rewardItemsVanilla = rewardItemsVanilla;
    }

    public static Reward getByName(String name){
        for(Reward reward : get()){
            if(reward.name.equals(name)){
                return reward;
            }
        }
        return null;
    }

    public void rewardTo(Player player){
        Inventory playerInv = player.getInventory();
        Location playerLoc = player.getLocation();
        boolean isFull = playerInv.firstEmpty() == -1;
        for(ItemStack item : rewardItemsVanilla){
            if(isFull){
                playerLoc.getWorld().dropItem(playerLoc, item);
            }
            playerInv.addItem(item);
            isFull = playerInv.firstEmpty() == -1;
        }

        for(CustomItem item : rewardItemsCustom.keySet()){
            int amount = rewardItemsCustom.get(item);
            ItemStack itemStack = item.cloneItem(amount);
            if(isFull){
                playerLoc.getWorld().dropItem(playerLoc, itemStack);
            }
            playerInv.addItem(itemStack);
            isFull = playerInv.firstEmpty()  == -1;
        }

        if(money > 0){
            DailyRewards.economy.depositPlayer(player, money);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRefreshMinutes() {
        return refreshMinutes;
    }

    public void setRefreshMinutes(int refreshMinutes) {
        this.refreshMinutes = refreshMinutes;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Map<CustomItem, Integer> getRewardItemsCustom() {
        return rewardItemsCustom;
    }

    public void setRewardItemsCustom(Map<CustomItem, Integer> rewardItemsCustom) {
        this.rewardItemsCustom = rewardItemsCustom;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public static synchronized void add(Reward reward){
        rewardSet.add(reward);
    }

    public static synchronized Set<Reward> get(){
        return rewardSet;
    }
}
