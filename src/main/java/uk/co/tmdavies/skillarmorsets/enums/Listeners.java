package uk.co.tmdavies.skillarmorsets.enums;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.listeners.PlayerListener;

public enum Listeners {

    PlayerListener(new PlayerListener(getPlugin()));

    Listener list;

    Listeners(Listener list) {
        this.list = list;
    }

    public Listener getListener() {
        return list;
    }

    public static SkillArmorSets getPlugin() {
        return JavaPlugin.getPlugin(SkillArmorSets.class);
    }
}
