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

public class MobCoinBoots {

    private ItemStack boots;
    private ItemMeta bMeta;
    private List<String> lore;
    private NamespacedKey mobcoinKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "mobcoinset");
    private NamespacedKey levelKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "level");
    private NamespacedKey xpKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "xp");

    public MobCoinBoots() {
        boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        bMeta = boots.getItemMeta();

        bMeta.setDisplayName(Utils.Chat("&eMobcoin Boots"));

        bMeta.getPersistentDataContainer().set(mobcoinKey, PersistentDataType.STRING, "mobcoinset");
        bMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, 1);
        bMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, 0);

        boots.setItemMeta(bMeta);

        lore = new ArrayList<>();
        int levelReq = (bMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.add("");
        lore.add(Utils.Chat("&7Level: &b" + bMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)));
        lore.add(Utils.Chat("&7XP: &b" + bMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER)) + "/" + levelReq);
        lore.add(ItemUtils.getProgressBar(0, levelReq, getXp()));
        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more mobcoins."));

        bMeta.setLore(lore);
        bMeta.setUnbreakable(true);

        boots.setItemMeta(bMeta);
    }

    public MobCoinBoots(String displayName, List<String> lore, int level, int xp) {
        boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        bMeta = boots.getItemMeta();
        bMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        bMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);

        bMeta.setDisplayName(displayName);
        bMeta.setLore(lore);
        bMeta.setUnbreakable(true);
        boots.setItemMeta(bMeta);
    }

    public ItemStack getBoots() {
        return boots;
    }

    public ItemMeta getItemMeta() {
        return bMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> newLore) {
        bMeta.setLore(newLore);
        boots.setItemMeta(bMeta);
    }

    public int getLevel() {
        return bMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
    }

    public void setLevel(int amount) {
        int level = bMeta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        level += amount;
        bMeta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, level);
        boots.setItemMeta(bMeta);
    }

    public int getXp() {
        return bMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
    }

    public void setXp(int amount) {
        int xp = bMeta.getPersistentDataContainer().get(xpKey, PersistentDataType.INTEGER);
        xp += amount;
        bMeta.getPersistentDataContainer().set(xpKey, PersistentDataType.INTEGER, xp);
        boots.setItemMeta(bMeta);
    }

}
