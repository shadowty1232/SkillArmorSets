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

        String query = "SELECT * FROM `" + sql.getDatabase() + "`.`MobCoinSword` WHERE `UUID` = ?";
        try {
            PreparedStatement grabState = sql.getConnection().prepareStatement(query);
            grabState.setString(1, player.getUniqueId().toString());
            ResultSet set = grabState.executeQuery();
            plugin.getLogger().info(String.valueOf(MySQL.rowCount(set)));
            swordLevel = set.getInt("LEVEL");
            swordXp = set.getInt("XP");
        } catch (SQLException e) {
            plugin.getLogger().info(Level.SEVERE + " Sword Info Grab Failure");
            player.sendMessage(Utils.Chat("&c" + Level.SEVERE + " Sword Info Grab Failure"));
            e.printStackTrace();
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

    public boolean isEnabled() {
        return setEnabled;
    }

    public void setEnabled(boolean enabled) {
        setEnabled = enabled;
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

        getSword().refreshSword(player);

        player.getInventory().setHelmet(helmet.getHelmet());
        player.getInventory().setChestplate(chestplate.getChestplate());
        player.getInventory().setLeggings(leggings.getLeggings());
        player.getInventory().setBoots(boots.getBoots());
        player.getInventory().addItem(sword.getSword());
        setEnabled = true;
        player.sendMessage(Utils.Chat("&aYou have been giving set MobCoin."));
    }

    public void removeSet() {
        if (!setEnabled) {
            player.sendMessage(Utils.Chat("&cYou do not have a set equipped."));
        }

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
        if (player.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
            player.getInventory().setHelmet(null);
        }
        if (player.getInventory().getChestplate().getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
            player.getInventory().setChestplate(null);
        }
        if (player.getInventory().getLeggings().getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
            player.getInventory().setLeggings(null);
        }
        if (player.getInventory().getBoots().getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
            player.getInventory().setBoots(null);
        }
        setEnabled = false;
    }

}
