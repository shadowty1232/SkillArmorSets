package uk.co.tmdavies.skillarmorsets.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sets.FarmerSet;
import uk.co.tmdavies.skillarmorsets.sets.MobCoinSet;
import uk.co.tmdavies.skillarmorsets.sets.farmset.FarmerHoe;
import uk.co.tmdavies.skillarmorsets.sets.mobcoinset.MobCoinSword;
import uk.co.tmdavies.skillarmorsets.sql.MobCoins;
import uk.co.tmdavies.skillarmorsets.sql.MySQL;
import uk.co.tmdavies.skillarmorsets.utils.Config;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import javax.naming.Name;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class PlayerListener implements Listener {

    private final SkillArmorSets plugin;
    private MySQL sql;
    private Config data;
    private NamespacedKey levelKey;
    private NamespacedKey mobcoinKey;
    private NamespacedKey farmerKey;
    private Random random;
    private MobCoins mobCoins;
    private Config lang;

    public PlayerListener(SkillArmorSets plugin) {
        this.plugin = plugin;
        data = plugin.data;
        sql = plugin.sql;
        lang = plugin.lang;
        mobCoins = plugin.mobCoins;
        random = new Random();
        levelKey = new NamespacedKey(plugin, "level");
        mobcoinKey = new NamespacedKey(plugin, "mobcoinset");
        farmerKey = new NamespacedKey(plugin, "farmerset");

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        String swordCheck = "SELECT * FROM `" + sql.getDatabase() + "`.`MobCoinSword` WHERE `uuid` = ? LIMIT 1";
        try (PreparedStatement checkState = sql.getConnection().prepareStatement(swordCheck)) {
            checkState.setString(1, p.getUniqueId().toString());
            ResultSet set = checkState.executeQuery();
            if (MySQL.rowCount(set) == 0) {
                MobCoinSet coinSet = new MobCoinSet(p);
                coinSet.getSword().fillSwordTable(p);

                plugin.mcSetStorage.put(p, coinSet);
            } else {
                MobCoinSet coinSet = new MobCoinSet(p, true);

                plugin.mcSetStorage.put(p, coinSet);
            }
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }

        String query = "SELECT * FROM `" + sql.getDatabase() + "`.`FarmerHoe` WHERE `uuid` = ? LIMIT 1";
        try (PreparedStatement checkState = sql.getConnection().prepareStatement(query)) {
            checkState.setString(1, p.getUniqueId().toString());
            ResultSet set = checkState.executeQuery();
            if (MySQL.rowCount(set) == 0) {
                FarmerSet farmerSet = new FarmerSet(p);
                farmerSet.getHoe().fillHoeTable(p);

                plugin.farmerSetStorage.put(p, farmerSet);
            } else {
                FarmerSet farmerSet = new FarmerSet(p, true);

                plugin.farmerSetStorage.put(p, farmerSet);
            }
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) throws SQLException {
        Player p = e.getPlayer();
        MobCoinSet mobCoinSet = plugin.mcSetStorage.get(p);
        mobCoinSet.getSword().saveSQL(p);

        if (mobCoinSet.isEnabled()) {
            mobCoinSet.removeSet();
        }

        FarmerSet farmerSet = plugin.farmerSetStorage.get(p);
        farmerSet.getHoe().saveSQL(p);

        if (farmerSet.isEnabled()) {
            farmerSet.removeSet();
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {

        if (!(e.getEntity().getKiller() instanceof Player)) return;
        Player p = e.getEntity().getKiller();
        if (!plugin.mcSetStorage.containsKey(p)) return;

        MobCoinSet set = plugin.mcSetStorage.get(p);
        MobCoinSword sword = set.getSword();

        if (!p.getInventory().getItemInMainHand().equals(sword.getSword())) return;

        if (e.getEntity() instanceof Player) {
            for (ItemStack item : e.getDrops()) {
                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                if (container.has(mobcoinKey, PersistentDataType.STRING) || container.has(levelKey, PersistentDataType.INTEGER)) {
                    item.setType(null);
                }
            }
            return;
        }

        sword.addXp(1);
        if (sword.getXp() == sword.getLevel() * 100) {
            sword.addLevel(1);
            sword.setXp(0);
        }

        sword.refreshSword(p);

        int coinsAmount = sword.getLevel();

        if (plugin.coinBuffer.containsKey(p)) {
            int temp = plugin.coinBuffer.get(p)+coinsAmount;
            plugin.coinBuffer.replace(p, temp);
        } else {
            plugin.coinBuffer.put(p, coinsAmount);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        FarmerSet set = plugin.farmerSetStorage.get(p);
        FarmerHoe hoe = set.getHoe();

        if (!p.getInventory().getItemInMainHand().equals(hoe.getHoe())) return;

        Material mat = e.getBlock().getType();

        if (mat.equals(Material.WHEAT) || mat.equals(Material.CACTUS)) {
            e.getBlock().setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(mat, 1 + hoe.getLevel()));
            hoe.addXp(1);
            if (hoe.getLevel() == hoe.getLevel() * 100) {
                hoe.addLevel(1);
                hoe.setXp(0);
            }
            hoe.refreshHoe(p);
        }
        if (mat.equals(Material.POTATOES)) {
            e.getBlock().setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.POTATO, 1 + hoe.getLevel()));
            hoe.addXp(1);
            if (hoe.getLevel() == hoe.getLevel() * 100) {
                hoe.addLevel(1);
                hoe.setXp(0);
            }
            hoe.refreshHoe(p);
        }
        if (mat.equals(Material.CARROTS)) {
            e.getBlock().setType(Material.AIR);
            p.getInventory().addItem(new ItemStack(Material.CARROT, 1 + hoe.getLevel()));
            hoe.addXp(1);
            if (hoe.getLevel() == hoe.getLevel() * 100) {
                hoe.addLevel(1);
                hoe.setXp(0);
            }
            hoe.refreshHoe(p);
        }
        if (mat.equals(Material.SUGAR_CANE)) {
            int caneCount = 1;
            Location loc = e.getBlock().getLocation();
            loc.getBlock().setType(Material.AIR);
            for (int i = 1; i < 3; i++) {
                loc.add(0, i, 0);
                if (loc.getBlock().getType().equals(Material.SUGAR_CANE)) {
                    loc.getBlock().setType(Material.AIR);
                    caneCount++;
                    continue;
                }
            }
            p.getInventory().addItem(new ItemStack(mat, caneCount * (1 + hoe.getLevel())));
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            ItemStack item = e.getCurrentItem();
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(mobcoinKey, PersistentDataType.STRING) || container.has(farmerKey, PersistentDataType.STRING)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(mobcoinKey, PersistentDataType.STRING) || container.has(farmerKey, PersistentDataType.STRING)
                || container.has(levelKey, PersistentDataType.INTEGER)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        MobCoinSet mobCoinSet = plugin.mcSetStorage.get(p);

        if (mobCoinSet.isEnabled()) {
            mobCoinSet.removeSet();
            mobCoinSet.getSword().refreshSword(p);
        }

        FarmerSet farmerSet = plugin.farmerSetStorage.get(p);

        if (farmerSet.isEnabled()) {
            farmerSet.removeSet();
            farmerSet.getHoe().refreshHoe(p);
        }
    }

}
