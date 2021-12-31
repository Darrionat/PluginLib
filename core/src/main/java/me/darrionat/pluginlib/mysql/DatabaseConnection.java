package me.darrionat.pluginlib.mysql;

import me.darrionat.pluginlib.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represents a MySQL database connection.
 */
public class DatabaseConnection {
    private final transient String host, database, username, password;
    private final transient int port;
    private final Plugin plugin;
    private Connection connection;

    /**
     * Prepares a connection to a database with no password requirement.
     *
     * @param host     The host to connect to
     * @param port     The port of the connection
     * @param database The database name
     * @param username The login username
     */
    public DatabaseConnection(String host, int port, String database, String username) {
        this(host, port, database, username, null);
    }

    /**
     * Prepares a connection to a database.
     *
     * @param host     The host to connect to
     * @param port     The port of the connection
     * @param database The database name
     * @param username The login username
     * @param password The login password
     */
    public DatabaseConnection(String host, int port, String database, String username, String password) {
        this.plugin = Plugin.getProject();
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    /**
     * Checks to see if a connection to the database is established.
     *
     * @return {@code true} if a connection exists and is open; {@code false} otherwise.
     */
    public boolean enabled() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException exe) {
            exe.printStackTrace();
            return false;
        }
    }

    /**
     * Connects to the defined database.
     *
     * @throws IllegalStateException Thrown if a connection already exists.
     */
    public void connect() {
        try {
            synchronized (plugin) {
                if (enabled()) {
                    throw new IllegalStateException("Connection already established");
                }
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        } catch (SQLException | ClassNotFoundException exe) {
            exe.printStackTrace();
        }
    }

    /**
     * Disconnects from the current database. If the connection is not enabled, nothing is done.
     */
    public boolean disconnect() {
        if (!enabled())
            return true;
        // Connection is not null since it is enabled
        try {
            connection.close();
            connection = null;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Prepares a SQL statement.
     *
     * @param sql The sql statement to prepare.
     * @return Returns a {@link PreparedStatement}.
     * @throws SQLException Thrown if a database error occurs.
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (!enabled())
            throw new IllegalStateException("Connection not enabled");
        return connection.prepareStatement(sql);
    }
}