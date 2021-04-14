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

public class FarmerBoots {

    private ItemStack boots;
    private ItemMeta bMeta;
    private List<String> lore;
    private NamespacedKey farmerKey = new NamespacedKey(JavaPlugin.getPlugin(SkillArmorSets.class), "farmerset");

    public FarmerBoots() {
        boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        bMeta = boots.getItemMeta();

        bMeta.setDisplayName(Utils.Chat("&eFarmer Boots"));

        bMeta.getPersistentDataContainer().set(farmerKey, PersistentDataType.STRING, "farmerset");
        boots.setItemMeta(bMeta);

        lore = new ArrayList<>();

        lore.add("");
        lore.add(Utils.Chat("&bFULL SET ABILITY"));
        lore.add(Utils.Chat("&7Wearing this set will allow you to gain"));
        lore.add(Utils.Chat("&7more crops while farming."));

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
