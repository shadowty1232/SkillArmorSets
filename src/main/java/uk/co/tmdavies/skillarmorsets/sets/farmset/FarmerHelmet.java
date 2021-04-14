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

public class FarmerHelmet {

    private ItemStack helmet;
    private ItemMeta hMeta;
    private List<String> lore;
    private NamespacedKey farmerKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "farmerset");

    public FarmerHelmet() {
        helmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        hMeta = helmet.getItemMeta();

        hMeta.setDisplayName(Utils.Chat("&eFarmer Helmet"));

        hMeta.getPersistentDataContainer().set(farmerKey, PersistentDataType.STRING, "farmerset");

        helmet.setItemMeta(hMeta);

        lore = new ArrayList<>();

        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more crops while farming."));

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
