package me.fluffycop.dailyrewards;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.*;

class YMLConfig {
    private DailyRewards plugin;

    private File configFile;
    private FileConfiguration config;

    //config sections
    private static final String ITEM_SECTION = "items";
    private static final String REWARD_SECTION = "rewards";

    //Field constants in items config section
    private static final String NAME_FIELD = "name";
    private static final String LORE_FIELD = "lore";
    private static final String ENCHANT_FIELD = "enchants";
    private static final String MATERIAL_FIELD = "material";

    //constants for reward section of config
    private static final String REFRESH_MINUTES_FIELD = "refresh-minutes";
    private static final String PERMISSION_FIELD = "permission";
    private static final String ITEMS_FIELD = "items";

    private static final String MONEY_MATERIAL = "MONEY";

    //Config properties, represented as fields
    private boolean enabled;

    public YMLConfig(DailyRewards plugin){
        this.plugin = plugin;
        try{
            firstRun();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        loadYamls();
        readConfig();
    }

    public void readConfig(){
        //read the enabled: <boolean> first
        this.enabled = config.getBoolean("enabled");

        //parse custom items from config
        ConfigurationSection itemSection = config.getConfigurationSection(ITEM_SECTION);
        for(String anItemStr : itemSection.getKeys(false)){
            ConfigurationSection anItem = config.getConfigurationSection(anItemStr);

            //skip this iteration if the material doesnt have a definition in material enum
            try {
                Material materialOf = Material.valueOf(anItem.getString(MATERIAL_FIELD));
                //material
                ItemStack item = new ItemStack(materialOf);
                ItemMeta anItemMeta = item.getItemMeta();
                item.setAmount(1);
                //name
                String itemName = anItem.getString(NAME_FIELD);
                anItemMeta.setDisplayName(itemName);
                //lore
                List<String> itemLore = anItem.getStringList(LORE_FIELD);
                anItemMeta.setLore(itemLore);
                //enchants
                for(String str : anItem.getStringList(ENCHANT_FIELD)){
                    try {
                        String[] enchantAndLevel = str.split(",");
                        Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantAndLevel[0]));
                        int level = Integer.parseInt(enchantAndLevel[1]);
                        anItemMeta.addEnchant(enchant, level, true);
                    } catch(Exception exception){
                        exception.printStackTrace();
                        continue;
                    }
                }

                CustomItem itemWrapper = new CustomItem();
                itemWrapper.setName(anItemStr);
                itemWrapper.setItem(item);

                DailyRewards.getCustomItemSet().add(itemWrapper);
            }catch(Exception exception){
                exception.printStackTrace();
                continue;
            }
        }

        //parse rewards from config
        ConfigurationSection rewardSection = config.getConfigurationSection(REWARD_SECTION);
        for(String rewardStr : rewardSection.getKeys(false)){
            //init a reward part of the config & config object
            ConfigurationSection aRewardSection = config.getConfigurationSection(rewardStr);
            Reward reward = new Reward();
            reward.setName(rewardStr);

            //get refresh minutes
            int minutes = aRewardSection.getInt(REFRESH_MINUTES_FIELD);
            reward.setRefreshMinutes(minutes);

            //get permissions required to claim reward
            String permission = aRewardSection.getString(PERMISSION_FIELD);
            reward.setPermission(permission);

            //parse the list of items
            Set<ItemStack> vanillaItemSet = new HashSet<>();
            Map<CustomItem, Integer> customItemMap = new HashMap<>();
            for(String materialStr : aRewardSection.getStringList(ITEMS_FIELD)){

                //array will always be length 2 since config is built that way
                String[] materialAmount = materialStr.split(",");
                CustomItem cItem = CustomItem.getByName(materialAmount[0]);

                if(materialAmount[0].equals(MONEY_MATERIAL) && plugin.isEcoEnabled()) {
                    try{
                        double moneyAmount = Double.parseDouble(materialAmount[1]);
                        reward.setMoney(moneyAmount);
                    }catch(Exception exception){
                        exception.printStackTrace();
                        continue;
                    }
                }else if(cItem != null){
                    try{
                        int cItemAmount = Integer.parseInt(materialAmount[1]);
                        customItemMap.put(cItem, cItemAmount);
                    }catch(Exception exception){
                        exception.printStackTrace();
                        continue;
                    }
                }else{
                    try{
                        Material material = Material.valueOf(materialAmount[0]);
                        int amount = Integer.parseInt(materialAmount[1]);

                        ItemStack item = new ItemStack(material);
                        item.setAmount(amount);

                        vanillaItemSet.add(item);
                    }catch(Exception exception){
                        exception.printStackTrace();
                        continue;
                    }
                }
            }
            if(vanillaItemSet.size() != 0){
                reward.setRewardItemsVanilla(vanillaItemSet);
            }
            if(customItemMap.size() != 0){
                reward.setRewardItemsCustom(customItemMap);
            }
        }
    }

    private void firstRun() throws Exception {
        if(!configFile.exists()){
            configFile.getParentFile().mkdirs();
            copy(plugin.getResource("config.yml"), configFile);
        }
    }
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveYamls() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadYamls() {
        try {
            config.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}