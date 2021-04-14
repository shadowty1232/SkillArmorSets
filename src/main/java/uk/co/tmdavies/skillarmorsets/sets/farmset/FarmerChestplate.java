package uk.co.tmdavies.skillarmorsets.sets.farmset;

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

public class FarmerChestplate {

    private ItemStack chestplate;
    private ItemMeta cMeta;
    private List<String> lore;
    private NamespacedKey farmerKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "farmerset");

    public FarmerChestplate() {
        chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        cMeta = chestplate.getItemMeta();

        cMeta.setDisplayName(Utils.Chat("&eFarmer Chestplate"));

        cMeta.getPersistentDataContainer().set(farmerKey, PersistentDataType.STRING, "farmerset");

        chestplate.setItemMeta(cMeta);

        lore = new ArrayList<>();

        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more crops while farming."));

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

}
