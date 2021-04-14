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

public class FarmerLeggings {

    private ItemStack leggings;
    private ItemMeta lMeta;
    private List<String> lore;
    private NamespacedKey farmerKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "farmerset");

    public FarmerLeggings() {
        leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        lMeta = leggings.getItemMeta();

        lMeta.setDisplayName(Utils.Chat("&eFarmer Leggings"));

        lMeta.getPersistentDataContainer().set(farmerKey, PersistentDataType.STRING, "farmerset");

        leggings.setItemMeta(lMeta);

        lore = new ArrayList<>();

        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more crops while farming."));

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

}
