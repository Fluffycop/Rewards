package me.doublealiu.dailyrewards;

import me.doublealiu.dailyrewards.DailyRewards;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class YMLConfig {
    private DailyRewards plugin;

    private final Set<ItemStack> customItems = new HashSet<>();

    private final Set<Reward> allRewards = new HashSet<>();

    private File configFile;
    private FileConfiguration config;

    //Field constants
    private static final String nameField = "name";
    private static final String loreField = "lore";
    private static final String enchantField = "enchants";
    private static final String materialField = "material";

    //Config properties, represented as fields
    private boolean enabled;

    public YMLConfig(DailyRewards plugin){
        this.plugin = plugin;
        try{
            firstRun();
        }catch(Exception exception){
            exception.printStackTrace();
        }
        readConfig();
    }

    private void readConfig(){
        //read the enabled: <boolean> first
        this.enabled = config.getBoolean("enabled");

        //parse custom items
        ConfigurationSection itemSection = config.getConfigurationSection("items:");
        for(String anItemStr : itemSection.getKeys(false)){
            ConfigurationSection anItem = config.getConfigurationSection(anItemStr);

            //skip this iteration if the material doesnt have a definition in material enum
            try {
                Material materialOf = Material.valueOf(anItem.getString(materialField));

                //init item stack with material
                ItemStack item = new ItemStack(materialOf);
                ItemMeta anItemMeta = item.getItemMeta();

                //name
                String itemName = anItem.getString(nameField);
                anItemMeta.setDisplayName(itemName);

                //lore
                List<String> itemLore = anItem.getStringList(loreField);
                anItemMeta.setLore(itemLore);

                //enchants
                try{
                    for(String str : anItem.getStringList(enchantField)){
                        String[] enchantAndLevel = str.split(",");
                        Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantAndLevel[0]));
                        int level = Integer.valueOf(enchantAndLevel[1]);
                        anItemMeta.addEnchant(enchant, level, true);
                    }
                }catch(IllegalArgumentException exception){
                    exception.printStackTrace();
                }finally {
                    continue;
                }

            }catch(IllegalArgumentException exception){
                exception.printStackTrace();
            }finally{
                continue;
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