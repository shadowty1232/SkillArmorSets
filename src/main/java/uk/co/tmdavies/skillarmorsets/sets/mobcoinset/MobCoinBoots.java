package uk.co.tmdavies.skillarmorsets.sets.mobcoinset;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MobCoinBoots {

    private ItemStack boots;
    private ItemMeta bMeta;
    private List<String> lore;
    private NamespacedKey mobcoinKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "mobcoinset");

    public MobCoinBoots() {
        boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        bMeta = boots.getItemMeta();

        bMeta.setDisplayName(Utils.Chat("&eMobcoin Boots"));

        bMeta.getPersistentDataContainer().set(mobcoinKey, PersistentDataType.STRING, "mobcoinset");
        boots.setItemMeta(bMeta);

        lore = new ArrayList<>();

        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more mobcoins."));

        bMeta.setLore(lore);
        bMeta.setUnbreakable(true);

        boots.setItemMeta(bMeta);
    }

    public MobCoinBoots(String displayName, List<String> lore) {
        boots = new ItemStack(Material.GOLDEN_BOOTS, 1);
        bMeta = boots.getItemMeta();

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
}
