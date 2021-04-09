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

public class MobCoinHelmet {

    private ItemStack helmet;
    private ItemMeta hMeta;
    private List<String> lore;
    private NamespacedKey mobcoinKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "mobcoinset");

    public MobCoinHelmet() {
        helmet = new ItemStack(Material.GOLDEN_HELMET, 1);
        hMeta = helmet.getItemMeta();

        hMeta.setDisplayName(Utils.Chat("&eMobcoin Helmet"));

        hMeta.getPersistentDataContainer().set(mobcoinKey, PersistentDataType.STRING, "mobcoinset");

        helmet.setItemMeta(hMeta);

        lore = new ArrayList<>();

        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more mobcoins."));

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

}
