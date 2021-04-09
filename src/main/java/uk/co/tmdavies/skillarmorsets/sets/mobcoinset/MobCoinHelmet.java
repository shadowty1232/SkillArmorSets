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

public class MobCoinHelmet {

    private ItemStack helmet;
    private ItemMeta hMeta;
    private List<String> lore;
    private NamespacedKey mobcoinKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "mobcoinset");
    private NamespacedKey levelKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "level");
    private NamespacedKey xpKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "xp");

    public MobCoinHelmet() {
        helmet = new ItemStack(Material.GOLDEN_HELMET, 1);
        hMeta = helmet.getItemMeta();

        hMeta.setDisplayName(Utils.Chat("&eMobcoin Helmet"));

        hMeta.getPersistentDataContainer().set(mobcoinKey, PersistentDataType.STRING, "mobcoinset");
        hMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, 1);
        hMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, 0);

        helmet.setItemMeta(hMeta);

        lore = new ArrayList<>();
        int levelReq = (hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.add("");
        lore.add(Utils.Chat("&7Level: &b" + hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)));
        lore.add(Utils.Chat("&7XP: &b" + hMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER)) + "/" + levelReq);
        lore.add(ItemUtils.getProgressBar(0, levelReq, getXp()));
        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more mobcoins."));

        hMeta.setLore(lore);

        hMeta.setUnbreakable(true);

        helmet.setItemMeta(hMeta);
    }

    public MobCoinHelmet(String displayName, List<String> lore, int level, int xp) {
        helmet = new ItemStack(Material.GOLDEN_HELMET, 1);
        hMeta = helmet.getItemMeta();
        hMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        hMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);

        hMeta.setDisplayName(displayName);
        hMeta.setLore(lore);
        hMeta.setUnbreakable(true);
        helmet.setItemMeta(hMeta);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemMeta getItemMeta() {
        return hMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> newLore) {
        hMeta.setLore(newLore);
        helmet.setItemMeta(hMeta);
    }

    public int getLevel() {
        return hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
    }

    public void setLevel(int amount) {
        int level = hMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        level += amount;
        hMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        helmet.setItemMeta(hMeta);
    }

    public int getXp() {
        return hMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
    }

    public void setXp(int amount) {
        int xp = hMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
        xp += amount;
        hMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);
        helmet.setItemMeta(hMeta);
    }
}
