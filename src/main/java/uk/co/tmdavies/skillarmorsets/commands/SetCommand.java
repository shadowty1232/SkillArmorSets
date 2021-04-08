package uk.co.tmdavies.skillarmorsets.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sets.MobCoinSet;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

public class SetCommand implements CommandExecutor {

    private final SkillArmorSets plugin;

    public SetCommand(SkillArmorSets plugin) {
        this.plugin = plugin;

        plugin.getCommand("set").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.Chat("&cOnly a player may execute this command!"));
            return true;
        }
        Player p = (Player) sender;
        if (p.hasPermission("skillarmorsets.set")) {
            if (args.length != 2) {
                p.sendMessage(Utils.Chat("&cInvalid Arguments:"));
                p.sendMessage(Utils.Chat("&c/set give <set>"));
                p.sendMessage(Utils.Chat("&c/set remove <set>"));
                return true;
            }
            switch(args[0].toLowerCase()) {
                case "give":
                    if (args[1].equalsIgnoreCase("mobcoin")) {
                        if (p.hasPermission("skillarmorsets.set.mobcoin")) {
                            MobCoinSet mobCoinSet = new MobCoinSet(p);
                            mobCoinSet.giveSet();
                            p.sendMessage(Utils.Chat("&aYou have been giving set MobCoin."));
                        }
                    }
                    break;
                case "remove":
                    if (args[1].equalsIgnoreCase("mobcoin")) {
                        if (p.hasPermission("skillarmorsets.set.mobcoin")) {
                            MobCoinSet mobCoinSet = new MobCoinSet(p);
                            mobCoinSet.removeSet();
                            p.sendMessage(Utils.Chat("&cYou have taken away set MobCoin."));
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;

        }

        return false;
    }
}