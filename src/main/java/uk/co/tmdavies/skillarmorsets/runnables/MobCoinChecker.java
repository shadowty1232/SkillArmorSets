package uk.co.tmdavies.skillarmorsets.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.sql.MobCoins;
import uk.co.tmdavies.skillarmorsets.utils.Utils;

import java.util.HashMap;

public class MobCoinChecker implements Runnable {

    private final SkillArmorSets plugin;
    private HashMap<Player, Integer> coinBuffer;
    private MobCoins mobCoins;
    int i = 0;
    public MobCoinChecker(SkillArmorSets plugin) {
        this.plugin = plugin;
        this.coinBuffer = plugin.coinBuffer;
        this.mobCoins = plugin.mobCoins;
    }

    @Override
    public void run() {
        if (coinBuffer.isEmpty()) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (coinBuffer.containsKey(p)) {
                mobCoins.addMobCoins(p, coinBuffer.get(p));
                p.sendMessage(Utils.Chat(plugin.lang.getString("MobCoins.Get")
                        .replace("%prefix%", Utils.Chat(plugin.lang.getString("Prefix"))))
                        .replace("%amount%", String.valueOf(coinBuffer.get(p))));
                coinBuffer.remove(p);
                i++;
            }
        }
        plugin.getLogger().info(Utils.Chat("&aAdded MobCoins To DataBase. (" + i + ")"));
        i = 0;
    }
}
