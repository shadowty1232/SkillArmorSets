package uk.co.tmdavies.skillarmorsets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.enums.Commands;
import uk.co.tmdavies.skillarmorsets.enums.Listeners;
import uk.co.tmdavies.skillarmorsets.runnables.MobCoinChecker;
import uk.co.tmdavies.skillarmorsets.sets.FarmerSet;
import uk.co.tmdavies.skillarmorsets.sets.MobCoinSet;
import uk.co.tmdavies.skillarmorsets.sql.MobCoins;
import uk.co.tmdavies.skillarmorsets.sql.MySQL;
import uk.co.tmdavies.skillarmorsets.sql.SQLUtils;
import uk.co.tmdavies.skillarmorsets.utils.Config;

import java.util.HashMap;

public final class SkillArmorSets extends JavaPlugin {

    public HashMap<Player, MobCoinSet> mcSetStorage;
    public HashMap<Player, FarmerSet> farmerSetStorage;

    public HashMap<Player, Integer> coinBuffer;

    public Config lang;
    public Config data;
    public MySQL sql;

    public MobCoins mobCoins;

    private double langVersion = 1.1;
    // Mob Coin Set
    // PvP Set
    // Farm Set (Sugar Coin)

    @Override
    public void onEnable() {
        this.mcSetStorage = new HashMap<>();
        this.farmerSetStorage = new HashMap<>();
        this.coinBuffer = new HashMap<>();
        this.sql = SQLUtils.setUpSQL();
        this.mobCoins = new MobCoins(this);
        this.data = new Config(this, "./plugins/SkillArmorSets/data.yml");
        this.lang = new Config(this, "./plugins/SkillArmorSets/lang.yml");
        SQLUtils.setUpTables();

        setUpLang();
        registerCommands();
        registerEvents();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new MobCoinChecker(this), 0L, 200L);
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!mcSetStorage.containsKey(p)) continue;
            mcSetStorage.get(p).removeSet();
        }
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

    public void setUpLang() {
        if (lang.getDouble("Version") != langVersion) {
            lang.set("Version", langVersion);
            lang.set("Prefix", "&8[&bSkillArmorSets&8]");

            lang.set("MobCoins.Get", "%prefix% &7You have gained &a%amount% &7Mobcoins.");
            lang.set("MobCoins.Equipped", "%prefix% &aYou have equipped the MobCoins Set");
            lang.set("MobCoins.Unequipped", "%prefix% &aYou have unequipped the MobCoins Set");

            lang.set("Farmer.Equipped", "%prefix% &aYou have equipped the Farmer Set");
            lang.set("Farmer.Unequipped", "%prefix% &aYou have unequipped the Farmer Set");
        }
    }
}
