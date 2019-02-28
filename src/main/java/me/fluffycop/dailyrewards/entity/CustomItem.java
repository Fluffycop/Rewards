package me.fluffycop.dailyrewards.entity;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomItem {
    private ItemStack item;
    private String name;

    private final static Set<CustomItem> customItemSet = Collections.synchronizedSet(new HashSet<CustomItem>());

    public CustomItem(){
        add(this);
    }

    public ItemStack cloneItem(int amount){
        ItemStack clone = new ItemStack(item.getType());

        ItemMeta itemMeta = item.getItemMeta();
        Map<Enchantment, Integer> itemEnchants = item.getEnchantments();

        clone.setAmount(amount);
        clone.setItemMeta(itemMeta);
        clone.addUnsafeEnchantments(itemEnchants);

        return clone;
    }

    public static CustomItem getByName(String name){
        for(CustomItem cItem : get()){
            if(cItem.getName().equals(name)){
                return cItem;
            }
        }
        return null;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static synchronized void add(CustomItem cItem){
        customItemSet.add(cItem);
    }

    public static synchronized Set<CustomItem> get(){
        return customItemSet;
    }
}
