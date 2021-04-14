package uk.co.tmdavies.skillarmorsets.sets.farmset;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sql.MySQL;
import uk.co.tmdavies.skillarmorsets.utils.ItemUtils;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmerHoe {

    private ItemStack hoe;
    private ItemMeta hMeta;
    private List<String> lore;
    private MySQL sql;
    private SkillArmorSets plugin = JavaPlugin.getPlugin(SkillArmorSets.class);
    private NamespacedKey hoeKey = new NamespacedKey(plugin, "hoe");
    private NamespacedKey levelKey = new NamespacedKey(plugin, "level");
    private NamespacedKey xpKey = new NamespacedKey(plugin, "xp");

    public FarmerHoe() {
        sql = plugin.sql;
        hoe = new ItemStack(Material.STONE_HOE, 1);
        hMeta = hoe.getItemMeta();

        hMeta.setDisplayName(Utils.Chat("&eFarmer Hoe"));

        hMeta.getPersistentDataContainer().set(hoeKey, PersistentDataType.STRING, "farmerhoe");
        hMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, 1);
        hMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, 0);

        hoe.setItemMeta(hMeta);

        lore = new ArrayList<>();
        int levelReq = (hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.add("");
        lore.add(Utils.Chat("&7Level: &b" + hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)));
        lore.add(Utils.Chat("&7XP: &b" + hMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER)) + "/" + levelReq);
        lore.add(ItemUtils.getProgressBar(0, levelReq, getXp()));
        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more crops while farming."));

        hMeta.setLore(lore);
        hMeta.setUnbreakable(true);

        hoe.setItemMeta(hMeta);
    }

    public FarmerHoe(int level, int xp) {
        hoe = new ItemStack(Material.STONE_HOE, 1);
        hMeta = hoe.getItemMeta();
        sql = plugin.sql;
        hMeta.setDisplayName(Utils.Chat("&eFarmer Hoe"));

        hMeta.getPersistentDataContainer().set(hoeKey, PersistentDataType.STRING, "farmerhoe");
        hMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        hMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);

        lore = new ArrayList<>();

        int levelReq = (hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.add("");
        lore.add(Utils.Chat("&7Level: &b" + hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)));
        lore.add(Utils.Chat("&7XP: &b" + hMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER)) + "/" + levelReq);
        lore.add(ItemUtils.getProgressBar(0, levelReq, getXp()));
        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more crops while farming."));

        hMeta.setLore(lore);
        hMeta.setUnbreakable(true);
        hoe.setItemMeta(hMeta);
    }

    public ItemStack getHoe() {
        return hoe;
    }

    public ItemMeta getItemMeta() {
        return hMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> newLore) {
        hMeta.setLore(newLore);
        hoe.setItemMeta(hMeta);
    }

    public int getLevel() {
        return hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
    }

    public void setLevel(int amount) {
        hMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, amount);
        hoe.setItemMeta(hMeta);
    }

    public void addLevel(int amount) {
        int level = hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        level += amount;
        hMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        hoe.setItemMeta(hMeta);
    }

    public int getXp() {
        return hMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
    }

    public void setXp(int amount) {
        hMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, amount);
        hoe.setItemMeta(hMeta);
    }

    public void addXp(int amount) {
        int xp = hMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
        xp += amount;
        hMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);
        hoe.setItemMeta(hMeta);
    }

    public void refreshHoe(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (item == null) continue;
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(hoeKey, PersistentDataType.STRING)) {
                item.setItemMeta(hMeta);
                player.getInventory().setItemInMainHand(ItemUtils.updateProgressBar(hoe));
                return;
            }
        }
    }

    public void saveSQL(Player p) throws SQLException {
        String query = "UPDATE `" + sql.getDatabase() + "`.`FarmerHoe` SET `level` = ?, `xp` = ? WHERE `UUID` = ?";
        try (PreparedStatement statement = sql.getConnection().prepareStatement(query)) {
            statement.setInt(1, getLevel());
            statement.setInt(2, getXp());
            statement.setString(3, p.getUniqueId().toString());

            statement.executeUpdate();
        }
    }

    public void fillHoeTable(Player p) {
        String check = "SELECT * FROM `" + sql.getDatabase() + "`.`FarmerHoe` WHERE `uuid` = ? LIMIT 1";
        try (PreparedStatement checkState = sql.getConnection().prepareStatement(check)) {
            checkState.setString(1, p.getUniqueId().toString());

            ResultSet set = checkState.executeQuery();

            if (MySQL.rowCount(set) == 0) {
                String insert = "INSERT INTO `" + sql.getDatabase() + "`.`FarmerHoe` (id, uuid, level, xp) VALUE (NULL, ?, ?, ?)";

                try (PreparedStatement insertState = sql.getConnection().prepareStatement(insert)) {
                    insertState.setString(1, p.getUniqueId().toString());
                    insertState.setInt(2, 1);
                    insertState.setInt(3, 0);

                    insertState.executeUpdate();
                } catch (SQLException e) {
                    plugin.getLogger().info(Utils.Chat("&cError Inserting FarmerHoe"));
                }
            }

        } catch (SQLException e) {
            plugin.getLogger().info(Utils.Chat("&cError Checking FarmerHoe"));
        }

    }
}
