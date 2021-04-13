package uk.co.tmdavies.skillarmorsets.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;

import java.util.List;

public class ItemUtils {

    private static final SkillArmorSets plugin = JavaPlugin.getPlugin(SkillArmorSets.class);
    private static NamespacedKey mobcoinKey = new NamespacedKey(plugin, "mobcoinset");
    private static NamespacedKey levelKey = new NamespacedKey(plugin, "level");
    private static NamespacedKey xpKey = new NamespacedKey(plugin, "xp");

    public static String getProgressBar(int min, int max, int value) {
        int amount = (int) Utils.map(min, max, 0, 30, value);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            if (i < amount) {
                builder.append(Utils.Chat("&a|"));
            } else {
                builder.append(Utils.Chat("&7|"));
            }
        }
        return builder.toString();
    }

    public static ItemStack updateProgressBar(ItemStack item) {
        ItemMeta iMeta = item.getItemMeta();
        PersistentDataContainer container = iMeta.getPersistentDataContainer();
        List<String> lore = iMeta.getLore();
        int levelReq = (container.get(levelKey, PersistentDataType.INTEGER) * 100);

        lore.set(1, Utils.Chat("&7Level: &b" + container.get(levelKey, PersistentDataType.INTEGER)));
        lore.set(2, Utils.Chat("&7XP: &b" + container.get(xpKey, PersistentDataType.INTEGER) + "/" + container.get(levelKey, PersistentDataType.INTEGER) * 100));
        lore.set(3, getProgressBar(0, levelReq, container.get(xpKey, PersistentDataType.INTEGER)));

        iMeta.setLore(lore);

        item.setItemMeta(iMeta);

        return item;
    }

    @Deprecated
    public static ItemStack gainXP(ItemStack item) {
        ItemMeta hMeta = item.getItemMeta();
        PersistentDataContainer container = hMeta.getPersistentDataContainer();
        NamespacedKey levelKey = new NamespacedKey(plugin, "level");
        NamespacedKey xpKey = new NamespacedKey(plugin, "xp");

        if (container.has(levelKey, PersistentDataType.INTEGER)) {
            int xpGiven = container.get(xpKey, PersistentDataType.INTEGER) + 1;
            container.set(xpKey, PersistentDataType.INTEGER, xpGiven);
            item.setItemMeta(hMeta);

            if (container.get(xpKey, PersistentDataType.INTEGER) == container.get(levelKey, PersistentDataType.INTEGER) * 100) {
                container.set(levelKey, PersistentDataType.INTEGER, container.get(levelKey, PersistentDataType.INTEGER) + 1);
                container.set(xpKey, PersistentDataType.INTEGER, 0);
                item.setItemMeta(hMeta);
            }
        } else {
            return item;
        }
        item.setItemMeta(hMeta);

        return item;
    }
}
