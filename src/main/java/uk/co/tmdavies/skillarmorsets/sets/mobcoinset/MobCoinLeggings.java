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

public class MobCoinLeggings {

    private ItemStack leggings;
    private ItemMeta lMeta;
    private List<String> lore;
    private NamespacedKey mobcoinKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "mobcoinset");
    private NamespacedKey levelKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "level");
    private NamespacedKey xpKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "xp");

    public MobCoinLeggings() {
        leggings = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
        lMeta = leggings.getItemMeta();

        lMeta.setDisplayName(Utils.Chat("&eMobcoin Leggings"));

        lMeta.getPersistentDataContainer().set(mobcoinKey, PersistentDataType.STRING, "mobcoinset");
        lMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, 1);
        lMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, 0);

        leggings.setItemMeta(lMeta);

        lore = new ArrayList<>();
        int levelReq = (lMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.add("");
        lore.add(Utils.Chat("&7Level: &b" + lMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)));
        lore.add(Utils.Chat("&7XP: &b" + lMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)) + "/" + levelReq);
        lore.add(ItemUtils.getProgressBar(0, levelReq, getXp()));
        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more mobcoins."));

        lMeta.setLore(lore);
        lMeta.setUnbreakable(true);

        leggings.setItemMeta(lMeta);
    }

    public MobCoinLeggings(String displayName, List<String> lore, int level, int xp) {
        leggings = new ItemStack(Material.GOLDEN_LEGGINGS, 1);
        lMeta = leggings.getItemMeta();
        lMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        lMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);

        lMeta.setDisplayName(displayName);
        lMeta.setLore(lore);
        lMeta.setUnbreakable(true);
        leggings.setItemMeta(lMeta);
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemMeta getItemMeta() {
        return lMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> newLore) {
        lMeta.setLore(newLore);
        leggings.setItemMeta(lMeta);
    }

    public int getLevel() {
        return lMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
    }

    public void setLevel(int amount) {
        int level = lMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        level += amount;
        lMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        leggings.setItemMeta(lMeta);
    }

    public int getXp() {
        return lMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
    }

    public void setXp(int amount) {
        int xp = lMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
        xp += amount;
        lMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);
        leggings.setItemMeta(lMeta);
    }

}
