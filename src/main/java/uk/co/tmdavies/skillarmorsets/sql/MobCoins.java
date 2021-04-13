package uk.co.tmdavies.skillarmorsets.sql;

import org.bukkit.entity.Player;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MobCoins {

    private final SkillArmorSets plugin;
    private MySQL sql;

    public MobCoins(SkillArmorSets plugin) {
        this.plugin = plugin;
        this.sql = plugin.sql;
    }

    public int getMobCoins(Player p) {
        String query = "SELECT * FROM `" + sql.getDatabase() + "`.`MobCoins` WHERE `UUID` = ? LIMIT 1";
        try {
            PreparedStatement grabState = sql.getConnection().prepareStatement(query);
            grabState.setString(1, p.getUniqueId().toString());

            ResultSet set = grabState.executeQuery();
            if (MySQL.rowCount(set) == 1) {
                return set.getInt("mob_coins");
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean setMobCoins(Player p, int amount) {
        String query = "UPDATE `" + sql.getDatabase() + "`.`MobCoins` SET `mob_coins` = ? WHERE `uuid` = ?";

        try {
            PreparedStatement updateState = sql.getConnection().prepareStatement(query);
            updateState.setInt(1, amount);
            updateState.setString(2, p.getUniqueId().toString());
            updateState.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean addMobCoins(Player p, int amount) {
        String query = "UPDATE `" + sql.getDatabase() + "`.`MobCoins` SET `mob_coins` = `mob_coins` + ? WHERE `uuid` = ?";

        try {
            PreparedStatement updateState = sql.getConnection().prepareStatement(query);
            updateState.setInt(1, amount);
            updateState.setString(2, p.getUniqueId().toString());
            updateState.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean removeMobCoins(Player p, int amount) {
        String query = "UPDATE `" + sql.getDatabase() + "`.`MobCoins` SET `mob_coins` = `mob_coins` - ? WHERE `uuid` = ?";

        try {
            PreparedStatement updateState = sql.getConnection().prepareStatement(query);
            updateState.setInt(1, amount);
            updateState.setString(2, p.getUniqueId().toString());
            updateState.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
