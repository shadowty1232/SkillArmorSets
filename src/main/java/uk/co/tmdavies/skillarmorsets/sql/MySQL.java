package uk.co.tmdavies.skillarmorsets.sql;

import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private final String encoding;
    private final boolean ssl;
    private Connection connection = null;

    public MySQL(String host, String port, String database, String username, String password, String encoding, boolean ssl) {

        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.encoding = encoding;
        this.ssl = ssl;

    }

    public static int rowCount(ResultSet results) throws SQLException {

        results.last();
        int rows = results.getRow();
        results.first();
        return rows;

    }

    public Connection getConnection() throws SQLException {

        if (this.connection != null) {
            if (!this.connection.isClosed() && this.connection.isValid(100)) {
                return this.connection;
            }
        }

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database +
                            "?useUnicode=yes&characterEncoding=" + this.encoding + "&useSSL=" + (this.ssl ? "true" : "false") + "&autoReconnect=true",
                    this.username, this.password);
            return this.connection;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("FAILED TO CONNECT TO DATABASE");
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            e.printStackTrace();
        }

        return null;

    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException ignored) {}
    }

    public String getHost() {
        return this.host;
    }

    public String getPort() {
        return this.port;
    }

    public String getDatabase() {
        return this.database;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public boolean isSSL() {
        return this.ssl;
    }

}
