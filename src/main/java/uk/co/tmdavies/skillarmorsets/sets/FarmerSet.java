package uk.co.tmdavies.skillarmorsets.sets;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sets.farmset.*;
import uk.co.tmdavies.skillarmorsets.sets.mobcoinset.*;
import uk.co.tmdavies.skillarmorsets.sql.MySQL;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class FarmerSet {

    private Player player;
    private FarmerHelmet helmet;
    private FarmerChestplate chestplate;
    private FarmerLeggings leggings;
    private FarmerBoots boots;
    private FarmerHoe hoe;
    private SkillArmorSets plugin = JavaPlugin.getPlugin(SkillArmorSets.class);
    private NamespacedKey farmerKey = new NamespacedKey(plugin, "farmerset");
    private NamespacedKey hoeKey = new NamespacedKey(plugin, "hoe");
    private MySQL sql = plugin.sql;
    private boolean setEnabled = false;


    public FarmerSet(Player player) {
        this.player = player;
        helmet = new FarmerHelmet();
        chestplate = new FarmerChestplate();
        leggings = new FarmerLeggings();
        boots = new FarmerBoots();
        hoe = new FarmerHoe();

        player.sendMessage(Utils.Chat("&aYou have been giving set MobCoin."));
    }

    public FarmerSet(Player player, boolean hasJoined) {

        if (!hasJoined) {
            new FarmerSet(player);
            return;
        }

        this.player = player;
        int hoeLevel;
        int hoeXp;

        String query = "SELECT * FROM `" + sql.getDatabase() + "`.`FarmerHoe` WHERE `uuid` = ?";
        try {
            PreparedStatement grabState = sql.getConnection().prepareStatement(query);
            grabState.setString(1, player.getUniqueId().toString());
            ResultSet set = grabState.executeQuery();
            plugin.getLogger().info(String.valueOf(MySQL.rowCount(set)));
            hoeLevel = set.getInt("level");
            hoeXp = set.getInt("xp");
        } catch (SQLException e) {
            plugin.getLogger().info(Level.SEVERE + " Hoe Info Grab Failure");
            player.sendMessage(Utils.Chat("&c" + Level.SEVERE + " Hoe Info Grab Failure"));
            e.printStackTrace();
            return;
        }

        helmet = new FarmerHelmet();
        chestplate = new FarmerChestplate();
        leggings = new FarmerLeggings();
        boots = new FarmerBoots();
        hoe = new FarmerHoe(hoeLevel, hoeXp);

    }

    public Player getPlayer() {
        return player;
    }

    public FarmerHelmet getHelmet() {
        return helmet;
    }

    public FarmerChestplate getChestplate() {
        return chestplate;
    }

    public FarmerLeggings getLeggings() {
        return leggings;
    }

    public FarmerBoots getBoots() {
        return boots;
    }

    public FarmerHoe getHoe() {
        return hoe;
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

        getHoe().refreshHoe(player);

        player.getInventory().setHelmet(helmet.getHelmet());
        player.getInventory().setChestplate(chestplate.getChestplate());
        player.getInventory().setLeggings(leggings.getLeggings());
        player.getInventory().setBoots(boots.getBoots());
        player.getInventory().addItem(hoe.getHoe());

        setEnabled = true;

        player.sendMessage(Utils.Chat(plugin.lang.getString("Farmer.Equipped")
                .replace("%prefix%", Utils.Chat(plugin.lang.getString("Prefix")))));
    }

    public void removeSet() {
        if (!setEnabled) {
            player.sendMessage(Utils.Chat("&cYou do not have a set equipped."));
        }

        if (player.getInventory().getSize() == 0) return;
        for (ItemStack item : player.getInventory()) {
            if (item == null) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(farmerKey, PersistentDataType.STRING)) {
                player.getInventory().remove(item);
            }
            if (item.getItemMeta().getPersistentDataContainer().has(hoeKey, PersistentDataType.STRING)) {
                player.getInventory().remove(item);
            }
        }
        if (player.getInventory().getHelmet().getItemMeta().getPersistentDataContainer().has(farmerKey, PersistentDataType.STRING)) {
            player.getInventory().setHelmet(null);
        }
        if (player.getInventory().getChestplate().getItemMeta().getPersistentDataContainer().has(farmerKey, PersistentDataType.STRING)) {
            player.getInventory().setChestplate(null);
        }
        if (player.getInventory().getLeggings().getItemMeta().getPersistentDataContainer().has(farmerKey, PersistentDataType.STRING)) {
            player.getInventory().setLeggings(null);
        }
        if (player.getInventory().getBoots().getItemMeta().getPersistentDataContainer().has(farmerKey, PersistentDataType.STRING)) {
            player.getInventory().setBoots(null);
        }
        setEnabled = false;

        player.sendMessage(Utils.Chat(plugin.lang.getString("Farmer.Unequipped")
                .replace("%prefix%", Utils.Chat(plugin.lang.getString("Prefix")))));
    }

}
