package uk.co.tmdavies.skillarmorsets.enums;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.commands.SetCommand;
import uk.co.tmdavies.skillarmorsets.commands.TestCommand;

public enum Commands {

    Set(new SetCommand(getPlugin())),
    Test(new TestCommand(getPlugin()));

    CommandExecutor cmdExe;

    Commands(CommandExecutor cmdExe) {
        this.cmdExe = cmdExe;
    }

    public CommandExecutor getCommandExecutor() {
        return cmdExe;
    }

    public static SkillArmorSets getPlugin() {
        return JavaPlugin.getPlugin(SkillArmorSets.class);
    }
}
