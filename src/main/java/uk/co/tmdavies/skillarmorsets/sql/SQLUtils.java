package uk.co.tmdavies.skillarmorsets.sql;

import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.skillarmorsets.SkillArmorSets;
import uk.co.tmdavies.skillarmorsets.utils.Config;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLUtils {

    private static SkillArmorSets plugin = JavaPlugin.getPlugin(SkillArmorSets.class);
    private static Config sqlDetails;
    private static MySQL sql;

    public static MySQL setUpSQL() {
        sqlDetails = new Config(plugin, "./plugins/SkillArmorSets/database.yml");
        if (sqlDetails.getString("Database.Host") == null) {
            sqlDetails.set("Database.Host", "51.89.138.80");
            sqlDetails.set("Database.Port", "3306");
            sqlDetails.set("Database.Username", "DoomFly_doomfly");
            sqlDetails.set("Database.Password", "LemonParty123!");
            sqlDetails.set("Database.Database", "DoomFly_skycore");
            sqlDetails.set("Database.Encoding", "UTF-8");
            sqlDetails.set("Database.SSL", "false");
        }

        String host = sqlDetails.getString("Database.Host");
        String port = sqlDetails.getString("Database.Port");
        String username = sqlDetails.getString("Database.Username");
        String password = sqlDetails.getString("Database.Password");
        String database = sqlDetails.getString("Database.Database");
        String encoding = sqlDetails.getString("Database.Encoding");
        boolean ssl = (sqlDetails.getString("Database.SSL").equals("true") ? true : false);

        if (sql != null) {
            sql.close();
        }

        sql = new MySQL(host, port, database, username, password, encoding, ssl);

        return sql;
    }

    public static void setUpTables() {
        createSwordTable();
        createHoeTable();
    }

    public static void createSwordTable() {
        String query = "CREATE TABLE IF NOT EXISTS `" + sql.getDatabase() + "`.`MobCoinSword` (" +
                "`id` INT(11) NOT NULL AUTO_INCREMENT, " +
                "`uuid` VARCHAR(36) NOT NULL, " +
                "`level` INT(11) NOT NULL, " +
                "`xp` INT(11) NOT NULL, " +
                "PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        try (PreparedStatement state = sql.getConnection().prepareStatement(query)) {
            state.execute();
        } catch (SQLException e) {
            JavaPlugin.getPlugin(SkillArmorSets.class).getLogger().info(Level.SEVERE + " SQL Fail MobCoinSword");
            e.printStackTrace();
        }
    }

    public static void createHoeTable() {
        String query = "CREATE TABLE IF NOT EXISTS `" + sql.getDatabase() + "`.`FarmerHoe` (" +
                "`id` INT(11) NOT NULL AUTO_INCREMENT, " +
                "`uuid` VARCHAR(36) NOT NULL, " +
                "`level` INT(11) NOT NULL, " +
                "`xp` INT(11) NOT NULL, " +
                "PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        try (PreparedStatement state = sql.getConnection().prepareStatement(query)) {
            state.execute();
        } catch (SQLException e) {
            JavaPlugin.getPlugin(SkillArmorSets.class).getLogger().info(Level.SEVERE + " SQL Fail FarmerHoe");
            e.printStackTrace();
        }
    }


}
