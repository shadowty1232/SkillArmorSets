package uk.co.tmdavies.skillarmorsets.sets.mobcoinset;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.utils.ItemUtils;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MobCoinChestplate {

    private ItemStack chestplate;
    private ItemMeta cMeta;
    private List<String> lore;
    private NamespacedKey mobcoinKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "mobcoinset");
    private NamespacedKey levelKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "level");
    private NamespacedKey xpKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "xp");

    public MobCoinChestplate() {
        chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE, 1);
        cMeta = chestplate.getItemMeta();

        cMeta.setDisplayName(Utils.Chat("&eMobcoin Chestplate"));

        cMeta.getPersistentDataContainer().set(mobcoinKey, PersistentDataType.STRING, "mobcoinset");
        cMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, 1);
        cMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, 0);

        chestplate.setItemMeta(cMeta);

        lore = new ArrayList<>();
        int levelReq = (cMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.add("");
        lore.add(Utils.Chat("&7Level: &b" + cMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)));
        lore.add(Utils.Chat("&7XP: &b" + cMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER)) + "/" + levelReq);
        lore.add(ItemUtils.getProgressBar(0, levelReq, getXp()));
        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more mobcoins."));

        cMeta.setLore(lore);
        cMeta.setUnbreakable(true);

        chestplate.setItemMeta(cMeta);
    }

    public MobCoinChestplate(String displayName, List<String> lore, int level, int xp) {
        chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE, 1);
        cMeta = chestplate.getItemMeta();

        cMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        cMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);

        cMeta.setDisplayName(displayName);
        cMeta.setLore(lore);
        cMeta.setUnbreakable(true);
        chestplate.setItemMeta(cMeta);
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemMeta getItemMeta() {
        return cMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> newLore) {
        cMeta.setLore(newLore);
        chestplate.setItemMeta(cMeta);
    }

    public int getLevel() {
        return cMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
    }

    public void setLevel(int amount) {
        int level = cMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        level += amount;
        cMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        chestplate.setItemMeta(cMeta);
    }

    public int getXp() {
        return cMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
    }

    public void setXp(int amount) {
        int xp = cMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
        xp += amount;
        cMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);
        chestplate.setItemMeta(cMeta);
    }

}
