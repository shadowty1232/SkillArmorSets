package uk.co.tmdavies.skillarmorsets.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.utils.ItemUtils;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

public class TestCommand implements CommandExecutor {

    public SkillArmorSets plugin;

    public TestCommand(SkillArmorSets plugin) {
        this.plugin = plugin;
        plugin.getCommand("test").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;

        if (args.length == 3) {
            p.sendMessage(ItemUtils.getProgressBar(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2])));
            return true;
        }

        return false;
    }
}
 