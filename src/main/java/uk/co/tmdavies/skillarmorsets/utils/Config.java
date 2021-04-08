package uk.co.tmdavies.skillarmorsets.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    String path;
    File configFile;
    YamlConfiguration config;
    Plugin plugin;

    // Create File in Constructor

    public Config(Plugin plugin, String filePath) {
        this.plugin = plugin;
        this.path = filePath;
        this.configFile = new File(filePath);

        checkParentFolder();
        createFile();

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public boolean checkParentFolder() {
        if (!plugin.getDataFolder().exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                return true;
            } catch (RuntimeException e) {
                plugin.getLogger().info(ChatColor.RED + "Error creating Plugin Folder \n" + plugin.getDataFolder().getAbsolutePath());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean createFile() {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                return true;
            } catch (IOException e) {
                plugin.getLogger().info(ChatColor.RED + "Error creating Config File \n" + path);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return configFile;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getServer().getConsoleSender()
                    .sendMessage("Error saving Config at: \n" + path);
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
        reloadConfig();
    }

    public String getString(String path) {
        String string = config.getString(path);

        return string;
    }

    public Integer getInt(String path) {
        int i = config.getInt(path);

        return i;
    }

    public Double getDouble(String path) {
        double d = config.getDouble(path);

        return d;
    }

    public Boolean getBoolean(String path) {
        boolean b = config.getBoolean(path);

        return b;
    }

//    public Location getLocation(String path) {
//        Location loc = config.getLocation(path);
//
//        return loc;
//    }

    public ItemStack getItemStack(String path) {
        ItemStack item = config.getItemStack(path);

        return item;
    }

    public List getList(String path) {
        List list = config.getList(path);

        return list;
    }

    public List<String> getStringList(String path) {
        List<String> list = config.getStringList(path);

        return list;
    }

    public String toString() {
        return configFile.getAbsolutePath();
    }

}