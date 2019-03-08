package me.fluffycop.dailyrewards.command;

import me.fluffycop.dailyrewards.entity.CustomPlayer;
import me.fluffycop.dailyrewards.entity.Reward;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class DRCommandMessages {
    //list out all commands
    public static final String HELP_MSG =
                            ChatColor.GRAY + "/reward claim <reward> " + ChatColor.YELLOW + "Claim a reward with specified name\n" +
                            ChatColor.GRAY + "/reward list " + ChatColor.YELLOW + "List all rewards\n" +
                            ChatColor.GRAY + "/reward help " + ChatColor.YELLOW + "Displays this help prompt";;

    public static final String NONPLAYER_SENDER = ChatColor.RED + "/reward can only be executed by a player";

    public static final String NO_PERMISSION = ChatColor.RED + "You don't have permission to do that";

    //reward claim <reward>
    public static final String CLAIM_HELP = ChatColor.RED + "/reward claim <reward>";

    private static final String NO_REWARD_STR = "The reward %reward% does not exist";
    public static final String NO_REWARD_FOUND(String reward){
        return ChatColor.RED + NO_REWARD_STR.replaceAll("%reward%", reward);
    }

    private static final String SUCCESS_REWARD_STR = "You have been claimed the reward %reward%";
    public static final String SUCCESS_REWARD_CLAIM(String reward){
        return ChatColor.GREEN + SUCCESS_REWARD_STR.replaceAll("%reward%", reward);
    }

    private static final String COOLDOWN_STR = ChatColor.RED + "The reward %reward%'s cooldown has not completed";
    public static final String COOLDOWN_ERROR(String reward){
        return ChatColor.GREEN + SUCCESS_REWARD_STR.replaceAll("%reward%", reward);
    }

    //reward list
    public static final String DISPLAY_REWARDS(Player player, CustomPlayer cPlayer) {
        String str = "";
        for (String rewardStr : cPlayer.getRewardMinutesMap().keySet()) {
            Reward reward = Reward.getByName(rewardStr);
            if (player.hasPermission(reward.getPermission())) {
                str = str + ChatColor.GRAY + rewardStr + " - ";
                int i = cPlayer.getRewardMinutesMap().get(rewardStr);
                if (i == 0) {
                    str = str + ChatColor.GREEN + "READY\n";
                } else {
                    str = str + ChatColor.RED + formatTime(i) + "\n";
                }
            }
        }
        return str;
    }
    private static final String formatTime(int minutes){
        int hours = minutes/60;
        int mins = minutes%60;

        String hourStr = hours + "h";
        String minStr = mins + "m";
        if(hours == 0){
            hourStr = "";
        }
        if(mins == 0){
            minStr = "";
        }
        return hourStr + ":" + minStr;
    }
}