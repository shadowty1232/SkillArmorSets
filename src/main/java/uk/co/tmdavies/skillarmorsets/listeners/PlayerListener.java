package uk.co.tmdavies.skillarmorsets.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sets.MobCoinSet;
import uk.co.tmdavies.skillarmorsets.sets.mobcoinset.MobCoinSword;
import uk.co.tmdavies.skillarmorsets.utils.Config;

import java.sql.SQLException;

public class PlayerListener implements Listener {

    private final SkillArmorSets plugin;
    private Config data;
    private NamespacedKey levelKey;

    public PlayerListener(SkillArmorSets plugin) {
        this.plugin = plugin;
        data = plugin.data;
        levelKey = new NamespacedKey(plugin, "level");

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (data.getString("MobCoinSet." + e.getPlayer().getUniqueId().toString()) == null) {
            MobCoinSet set = new MobCoinSet(e.getPlayer());
            set.getSword().fillSwordTable(e.getPlayer());

            plugin.mcSetStorage.put(e.getPlayer(), set);
        } else {
            MobCoinSet set = new MobCoinSet(e.getPlayer(), true);

            plugin.mcSetStorage.put(e.getPlayer(), set);
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

        sword.addXp(1);
        if (sword.getXp() == sword.getLevel() * 100) {
            sword.addLevel(1);
            sword.setXp(0);
        }

        sword.refreshSword(p);

    }

}
