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
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sets.MobCoinSet;
import uk.co.tmdavies.skillarmorsets.utils.Config;
import uk.co.tmdavies.skillarmorsets.utils.ItemUtils;

import javax.naming.Name;

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

            plugin.mcSetStorage.put(e.getPlayer(), set);
        } else {
            MobCoinSet set = new MobCoinSet(e.getPlayer(), true);

            plugin.mcSetStorage.put(e.getPlayer(), set);
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        MobCoinSet set = plugin.mcSetStorage.get(p);

        data.set("MobCoinSet." + p.getUniqueId() + ".Helmet.DisplayName", set.getHelmet().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + p.getUniqueId() + ".Chestplate.DisplayName", set.getChestplate().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + p.getUniqueId() + ".Leggings.DisplayName", set.getLeggings().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + p.getUniqueId() + ".Boots.DisplayName", set.getBoots().getItemMeta().getDisplayName());
        data.set("MobCoinSet." + p.getUniqueId() + ".Sword.DisplayName", set.getSword().getItemMeta().getDisplayName());

        data.set("MobCoinSet." + p.getUniqueId() + ".Helmet.Lore", set.getHelmet().getLore());
        data.set("MobCoinSet." + p.getUniqueId() + ".Chestplate.Lore", set.getChestplate().getLore());
        data.set("MobCoinSet." + p.getUniqueId() + ".Leggings.Lore", set.getLeggings().getLore());
        data.set("MobCoinSet." + p.getUniqueId() + ".Boots.Lore", set.getBoots().getLore());
        data.set("MobCoinSet." + p.getUniqueId() + ".Sword.Lore", set.getSword().getLore());

        data.set("MobCoinSet." + p.getUniqueId() + ".Helmet.Level", set.getHelmet().getLevel());
        data.set("MobCoinSet." + p.getUniqueId() + ".Chestplate.Level", set.getChestplate().getLevel());
        data.set("MobCoinSet." + p.getUniqueId() + ".Leggings.Level", set.getLeggings().getLevel());
        data.set("MobCoinSet." + p.getUniqueId() + ".Boots.Level", set.getBoots().getLevel());
        data.set("MobCoinSet." + p.getUniqueId() + ".Sword.Level", set.getSword().getLevel());

        data.set("MobCoinSet." + p.getUniqueId() + ".Helmet.XP", set.getHelmet().getXp());
        data.set("MobCoinSet." + p.getUniqueId() + ".Chestplate.XP", set.getChestplate().getXp());
        data.set("MobCoinSet." + p.getUniqueId() + ".Leggings.XP", set.getLeggings().getXp());
        data.set("MobCoinSet." + p.getUniqueId() + ".Boots.XP", set.getBoots().getXp());
        data.set("MobCoinSet." + p.getUniqueId() + ".Sword.XP", set.getSword().getXp());

        data.reloadConfig();
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (!(e.getEntity().getKiller() instanceof Player)) return;

        Player p = e.getEntity().getKiller();
        ItemStack hand = p.getInventory().getItemInMainHand();

        if (hand.getItemMeta().getPersistentDataContainer().has(levelKey, PersistentDataType.INTEGER)) {
            p.getInventory().remove(hand);
            p.getInventory().addItem(ItemUtils.gainXP(hand));
        }

    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent e) {
        if (!(e.getInventory().getType() == InventoryType.PLAYER)) return;
        Player p = (Player) e.getPlayer();

        for (ItemStack item : p.getInventory()) {
            if (item == null) continue;
            if (!(item.getItemMeta().getPersistentDataContainer().has(levelKey, PersistentDataType.INTEGER))) continue;
            p.getInventory().remove(item);
            p.getInventory().addItem(ItemUtils.updateProgressBar(item));
        }
    }
}
