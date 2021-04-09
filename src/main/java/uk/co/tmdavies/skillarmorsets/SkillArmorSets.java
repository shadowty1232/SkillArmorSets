package uk.co.tmdavies.skillarmorsets;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.enums.Commands;
import uk.co.tmdavies.skillarmorsets.enums.Listeners;
import uk.co.tmdavies.skillarmorsets.sets.MobCoinSet;
import uk.co.tmdavies.skillarmorsets.sql.MySQL;
import uk.co.tmdavies.skillarmorsets.sql.SQLUtils;
import uk.co.tmdavies.skillarmorsets.utils.Config;

import java.util.HashMap;

public final class SkillArmorSets extends JavaPlugin {

    public HashMap<Player, MobCoinSet> mcSetStorage;

    public Config data;
    public MySQL sql;
    // Mob Coin Set
    // PvP Set
    // Farm Set (Sugar Coin)

    @Override
    public void onEnable() {
        this.mcSetStorage = new HashMap<>();
        this.sql = SQLUtils.setUpSQL();
        this.data = new Config(this, "./plugins/SkillArmorSets/data.yml");
        SQLUtils.createSwordTable();

        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        for (Commands cmd : Commands.values()) {
            cmd.getCommandExecutor();
        }
    }

    public void registerEvents() {
        for (Listeners list : Listeners.values()) {
            list.getListener();
        }
    }
}
