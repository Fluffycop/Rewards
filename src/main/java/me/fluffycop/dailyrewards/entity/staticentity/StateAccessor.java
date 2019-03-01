package me.fluffycop.dailyrewards.entity.staticentity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.fluffycop.dailyrewards.DailyRewards;
import me.fluffycop.dailyrewards.entity.CustomPlayer;
import me.fluffycop.dailyrewards.entity.Reward;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class StateAccessor {

    private final static String FILE_EXT = ".json";

    private DailyRewards plugin;
    private File dataFolder;

    public StateAccessor(DailyRewards plugin, File dataFolder){
        dataFolder.mkdir();
        this.plugin = plugin;
        this.dataFolder = dataFolder;
    }

    //grabs from DailyReward.DATA_FOLDER
    public void saveAll(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for(CustomPlayer player : CustomPlayer.get()){
            clean(player);
            File file = new File(dataFolder.getAbsolutePath() + File.separator + player.getPlayerID() + FILE_EXT);
            if(file.exists()){
                write(player, file, gson);
            }else{
                file.mkdir();
                write(player, file, gson);
            }
        }
    }

    //returns true if successfully loaded, returns false if not loaded
    public void loadAll(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for(File file : dataFolder.listFiles()){
            read(file,gson);
        }
    }

    //see usage in saveAll()
    private void write(CustomPlayer player, File file, Gson gson){
        String json = gson.toJson(player);
        //file input
        try(FileWriter writer = new FileWriter(file)){
            writer.write(json);
            writer.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void read(File file, Gson gson){
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            CustomPlayer player = gson.fromJson(reader, CustomPlayer.class);
            CustomPlayer.add(player);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void clean(CustomPlayer player){
        for(String rewardStr : player.getRewardMinutesMap().keySet()){
            if(Reward.getByName(rewardStr) == null){
                player.getRewardMinutesMap().remove(rewardStr);
            }
        }
    }
}
