package uk.co.tmdavies.skillarmorsets.sets;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sets.mobcoinset.*;
import uk.co.tmdavies.skillarmorsets.utils.Config;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.util.List;

public class MobCoinSet {

    private Player player;
    private MobCoinHelmet helmet;
    private MobCoinChestplate chestplate;
    private MobCoinLeggings leggings;
    private MobCoinBoots boots;
    private MobCoinSword sword;
    private NamespacedKey mobcoinKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "mobcoinset");

    public MobCoinSet(Player player) {
        this.player = player;
        helmet = new MobCoinHelmet();
        chestplate = new MobCoinChestplate();
        leggings = new MobCoinLeggings();
        boots = new MobCoinBoots();
        sword = new MobCoinSword();
    }

    public MobCoinSet(Player player, boolean hasJoined) {
        Config data = JavaPlugin.getPlugin(SkillArmorSets.class).data;

        if (!hasJoined) {
            new MobCoinSet(player);
            return;
        }

        this.player = player;

        String helmetName = data.getString("MobCoinSet." + player.getUniqueId().toString() + ".Helmet.DisplayName");
        String chestplateName = data.getString("MobCoinSet." + player.getUniqueId().toString() + ".Chestplate.DisplayName");
        String leggingsName = data.getString("MobCoinSet." + player.getUniqueId().toString() + ".Leggings.DisplayName");
        String bootsName = data.getString("MobCoinSet." + player.getUniqueId().toString() + ".Boots.DisplayName");
        String swordName = data.getString("MobCoinSet." + player.getUniqueId().toString() + ".Sword.DisplayName");

        List<String> helmetLore = data.getStringList("MobCoinSet." + player.getUniqueId().toString() + ".Helmet.Lore");
        List<String> chestplateLore = data.getStringList("MobCoinSet." + player.getUniqueId().toString() + ".Chestplate.Lore");
        List<String> leggingsLore = data.getStringList("MobCoinSet." + player.getUniqueId().toString() + ".Leggings.Lore");
        List<String> bootsLore = data.getStringList("MobCoinSet." + player.getUniqueId().toString() + ".Boots.Lore");
        List<String> swordLore = data.getStringList("MobCoinSet." + player.getUniqueId().toString() + ".Sword.Lore");

        int helmetLevel = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Helmet.Level");
        int chestplateLevel = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Chestplate.Level");
        int leggingsLevel = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Leggings.Level");
        int bootsLevel = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Boots.Level");
        int swordLevel = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Sword.Level");

        int helmetXp = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Helmet.XP");
        int chestplateXp = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Chestplate.XP");
        int leggingsXp = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Leggings.XP");
        int bootsXp = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Boots.XP");
        int swordXp = data.getInt("MobCoinSet." + player.getUniqueId().toString() + ".Sword.XP");

        helmet = new MobCoinHelmet(helmetName, helmetLore, helmetLevel, helmetXp);
        chestplate = new MobCoinChestplate(chestplateName, chestplateLore, chestplateLevel, chestplateXp);
        leggings = new MobCoinLeggings(leggingsName, leggingsLore, leggingsLevel, leggingsXp);
        boots = new MobCoinBoots(bootsName, bootsLore, bootsLevel, bootsXp);
        sword = new MobCoinSword(swordName, swordLore, swordLevel, swordXp);
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
    }

    public void removeSet() {
        if (player.getInventory().getSize() == 0) return;
        for (ItemStack item : player.getInventory()) {
            if (item == null) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
                player.getInventory().remove(item);
            }
        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item == null) continue;
            if (item.getItemMeta().getPersistentDataContainer().has(mobcoinKey, PersistentDataType.STRING)) {
                player.getInventory().remove(item);
            }
        }
    }

}
