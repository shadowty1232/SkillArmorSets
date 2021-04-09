package uk.co.tmdavies.skillarmorsets.sets.mobcoinset;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.utils.ItemUtils;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MobCoinSword {

    private ItemStack sword;
    private ItemMeta sMeta;
    private List<String> lore;
    private NamespacedKey swordKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "sword");
    private NamespacedKey levelKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "level");
    private NamespacedKey xpKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "xp");

    public MobCoinSword() {
        sword = new ItemStack(Material.GOLDEN_SWORD, 1);
        sMeta = sword.getItemMeta();

        sMeta.setDisplayName(Utils.Chat("&eMobcoin Sword"));

        sMeta.getPersistentDataContainer().set(swordKey, PersistentDataType.STRING, "mobcoinsword");
        sMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, 1);
        sMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, 0);

        sword.setItemMeta(sMeta);

        lore = new ArrayList<>();
        int levelReq = (sMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.add("");
        lore.add(Utils.Chat("&7Level: &b" + sMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)));
        lore.add(Utils.Chat("&7XP: &b" + sMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER)) + "/" + levelReq);
        lore.add(ItemUtils.getProgressBar(0, levelReq, getXp()));
        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more mobcoins."));

        sMeta.setLore(lore);
        sMeta.setUnbreakable(true);

        sword.setItemMeta(sMeta);
    }

    public MobCoinSword(String displayName, List<String> lore, int level, int xp) {
        sword = new ItemStack(Material.GOLDEN_SWORD, 1);
        sMeta = sword.getItemMeta();
        sMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        sMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);

        sMeta.setDisplayName(displayName);
        sMeta.setLore(lore);
        sMeta.setUnbreakable(true);
        sword.setItemMeta(sMeta);
    }

    public ItemStack getSword() {
        return sword;
    }

    public ItemMeta getItemMeta() {
        return sMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> newLore) {
        sMeta.setLore(newLore);
        sword.setItemMeta(sMeta);
    }

    public int getLevel() {
        return sMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
    }

    public void setLevel(int amount) {
        sMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, amount);
        sword.setItemMeta(sMeta);
    }

    public void addLevel(int amount) {
        int level = sMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        level += amount;
        sMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        sword.setItemMeta(sMeta);
    }

    public int getXp() {
        return sMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
    }

    public void setXp(int amount) {
        sMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, amount);
        sword.setItemMeta(sMeta);
    }

    public void addXp(int amount) {
        int xp = sMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
        xp += amount;
        sMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);
        sword.setItemMeta(sMeta);
    }

    public void refreshSword(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (item == null) continue;
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(swordKey, PersistentDataType.STRING)) {
                item.setItemMeta(sMeta);
                player.getInventory().setItemInMainHand(ItemUtils.updateProgressBar(sword));
                return;
            }
        }
    }
}
