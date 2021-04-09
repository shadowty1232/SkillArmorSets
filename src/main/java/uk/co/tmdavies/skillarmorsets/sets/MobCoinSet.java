package uk.co.tmdavies.skillarmorsets.sets;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sets.mobcoinset.*;
import uk.co.tmdavies.skillarmorsets.sql.MySQL;
import uk.co.tmdavies.skillarmorsets.utils.Config;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class MobCoinSet {

    private Player player;
    private MobCoinHelmet helmet;
    private MobCoinChestplate chestplate;
    private MobCoinLeggings leggings;
    private MobCoinBoots boots;
    private MobCoinSword sword;
    private SkillArmorSets plugin = JavaPlugin.getPlugin(SkillArmorSets.class);
    private NamespacedKey mobcoinKey = new NamespacedKey(plugin, "mobcoinset");
    private NamespacedKey swordKey = new NamespacedKey(plugin, "sword");
    private Config data = plugin.data;
    private MySQL sql = plugin.sql;
    private boolean setEnabled = false;


    public MobCoinSet(Player player) {
        this.player = player;
        helmet = new MobCoinHelmet();
        chestplate = new MobCoinChestplate();
        leggings = new MobCoinLeggings();
        boots = new MobCoinBoots();
        sword = new MobCoinSword();

        giveSet();

        player.sendMessage(Utils.Chat("&aYou have been giving set MobCoin."));
    }

    public MobCoinSet(Player player, boolean hasJoined) {

        if (!hasJoined) {
            new MobCoinSet(player);
            return;
        }

        this.player = player;

        int swordLevel;
        int swordXp;

        String query = "SELECT * FROM '" + sql.getDatabase() + "'.`MobCoinSword` WHERE `UUID` = ?";
        try (PreparedStatement grabState = sql.getConnection().prepareStatement(query)) {
            ResultSet set = grabState.executeQuery(query);
            swordLevel = set.getInt(2);
            swordXp = set.getInt(3);
        } catch (SQLException e) {
            plugin.getLogger().info(Level.SEVERE + " Sword Info Grab Failure");
            player.sendMessage(Utils.Chat("&c" + Level.SEVERE + " Sword Info Grab Failure"));
            return;
        }

        helmet = new MobCoinHelmet();
        chestplate = new MobCoinChestplate();
        leggings = new MobCoinLeggings();
        boots = new MobCoinBoots();
        sword = new MobCoinSword(swordLevel, swordXp);

    }

    public Player getPlayer() {
        return player;
    }

    public MobCoinHelmet getHelmet() {
        return helmet;
    }

    public MobCoinChestplate getChestplate() {
        return chestplate;
    }

    public MobCoinLeggings getLeggings() {
        return leggings;
    }

    public MobCoinBoots getBoots() {
        return boots;
    }

    public MobCoinSword getSword() {
        return sword;
    }

    public void giveSet() {
        if (setEnabled) {
            player.sendMessage(Utils.Chat("&cYou've already equipped your set."));
            return;
        }
        int emptySpaces = 0;
        for (ItemStack item : player.getInventory()) {
            if (item == null) emptySpaces++;
        }
        if (emptySpaces < 5) {
            player.sendMessage(Utils.Chat("&cYou do not have enough spaces"));
            return;
        }
        player.getInventory().addItem(helmet.getHelmet());
        player.getInventory().addItem(chestplate.getChestplate());
        player.getInventory().addItem(leggings.getLeggings());
        player.getInventory().addItem(boots.getBoots());
        player.getInventory().addItem(sword.getSword());
        setEnabled = true;
    }

    public void removeSet() {
        if (!setEnabled) {
            player.sendMessage(Utils.Chat("&cYou do not have a set equipped."));
        }
        data.set("MobCoinSet." + player.getUniqueId() + ".Helmet.DisplayName", getHelmet().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + player.getUniqueId() + ".Chestplate.DisplayName", getChestplate().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + player.getUniqueId() + ".Leggings.DisplayName", getLeggings().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + player.getUniqueId() + ".Boots.DisplayName", getBoots().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + player.getUniqueId() + ".Sword.DisplayName", getSword().getItemMeta().getDisplayName());

        data.set("MobCoinSet." + player.getUniqueId() + ".Helmet.Lore", getHelmet().getLore());
        data.set("MobCoinSet." + player.getUniqueId() + ".Chestplate.Lore", getChestplate().getLore());
        data.set("MobCoinSet." + player.getUniqueId() + ".Leggings.Lore", getLeggings().getLore());
        data.set("MobCoinSet." + player.getUniqueId() + ".Boots.Lore", getBoots().getLore());
        data.set("MobCoinSet." + player.getUniqueId() + ".Sword.Lore", getSword().getLore());

        data.set("MobCoinSet." + player.getUniqueId() + ".Sword.Level", getSword().getLevel());
        data.set("MobCoinSet." + player.getUniqueId() + ".Sword.XP", getSword().getXp());

        data.reloadConfig();

        if (player.getInventory().getSize() == 0) return;
        for (ItemStack item : player.getInventory()) {
            if (item == null) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
                player.getInventory().remove(item);
            }
            if (item.getItemMeta().getPersistentDataContainer().has(swordKey, PersistentDataType.STRING)) {
                player.getInventory().remove(item);
            }
        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
                player.getInventory().remove(item);
            }
        }
        setEnabled = false;
    }

}
