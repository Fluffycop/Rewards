package me.fluffycop.dailyrewards.command;

import me.fluffycop.dailyrewards.entity.CustomPlayer;
import me.fluffycop.dailyrewards.entity.Reward;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardCommandManager implements CommandExecutor {

    public static final String REWARD_CMD_ALIAS = "reward";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args){
        if(sender instanceof Player){
            if(!sender.hasPermission(DRCommandPermissions.REWARD_PERMISSION)){
                sender.sendMessage(DRCommandMessages.NO_PERMISSION);
                return true;
            }

            if(args.length == 0){
                sender.sendMessage(DRCommandMessages.HELP_MSG);
                return true;
            }

            Player player = (Player)sender;
            if(args[0].equals("claim")){
                if(!player.hasPermission(DRCommandPermissions.CLAIM_PERMISSION)){
                    sender.sendMessage(DRCommandMessages.NO_PERMISSION);
                    return true;
                }

                if(args.length < 2){
                    sender.sendMessage(DRCommandMessages.CLAIM_HELP);
                    return true;
                }else{
                    String rewardStr = args[1];
                    Reward reward = Reward.getByName(rewardStr);
                    //does reward exist
                    if(reward == null){
                        sender.sendMessage(DRCommandMessages.NO_REWARD_FOUND(rewardStr));
                        return true;
                    }
                    //does player have permission to access it
                    if(!player.hasPermission(reward.getPermission())){
                        sender.sendMessage(DRCommandMessages.NO_PERMISSION);
                        return true;
                    }

                    CustomPlayer cPlayer = CustomPlayer.getByID(player.getUniqueId().toString());
                    if(cPlayer.getRewardMinutesMap().get(reward.getName()) != 0){
                        sender.sendMessage(DRCommandMessages.COOLDOWN_ERROR(reward.getName()));
                        return true;
                    }

                    reward.rewardTo(player);
                    cPlayer.replace(reward.getName(), reward.getRefreshMinutes());
                    sender.sendMessage(DRCommandMessages.SUCCESS_REWARD_CLAIM(rewardStr));
                    return true;
                }
            }else if(args[0].equals("list")){
                if(!player.hasPermission(DRCommandPermissions.LIST_PERMISSION)){
                    sender.sendMessage(DRCommandMessages.NO_PERMISSION);
                    return true;
                }

                CustomPlayer cPlayer = CustomPlayer.getByID(player.getUniqueId().toString());
                sender.sendMessage(DRCommandMessages.DISPLAY_REWARDS(player,cPlayer));
                return true;
            }else{
                sender.sendMessage(DRCommandMessages.HELP_MSG);
                return true;
            }
        }
        sender.sendMessage(DRCommandMessages.NONPLAYER_SENDER);
        return true;
    }
}
