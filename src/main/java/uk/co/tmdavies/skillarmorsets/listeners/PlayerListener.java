package uk.co.tmdavies.skillarmorsets.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
import uk.co.tmdavies.skillarmorsets.sets.MobCoinSet;
import uk.co.tmdavies.skillarmorsets.sets.mobcoinset.MobCoinSword;
import uk.co.tmdavies.skillarmorsets.sql.MobCoins;
import uk.co.tmdavies.skillarmorsets.sql.MySQL;
import uk.co.tmdavies.skillarmorsets.utils.Config;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

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

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        String query = "SELECT * FROM `" + sql.getDatabase() + "`.`MobCoinSword` WHERE `UUID` = ? LIMIT 1";

        try (PreparedStatement checkState = sql.getConnection().prepareStatement(query)) {
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

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) throws SQLException {
        Player p = e.getPlayer();
        MobCoinSet set = plugin.mcSetStorage.get(p);
        set.getSword().saveSQL(p);

        set.removeSet();
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
    public void onClick(InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            ItemStack item = e.getCurrentItem();
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(mobcoinKey, PersistentDataType.STRING)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(mobcoinKey, PersistentDataType.STRING) || container.has(levelKey, PersistentDataType.INTEGER)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        MobCoinSet set = plugin.mcSetStorage.get(p);
        if (set.isEnabled()) {
            set.removeSet();
            set.getSword().refreshSword(p);
        }
    }

}
