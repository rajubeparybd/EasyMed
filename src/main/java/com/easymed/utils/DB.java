package com.easymed.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB for handling database connection
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class DB {
    private Connection connection;

    /**
     * Create a new DB instance
     */
    public DB() {
        this.connection = getConnection();
    }

    /**
     * Get the connection to the database
     *
     * @return Connection
     */
    public Connection getConnection() {
        Dotenv env = Dotenv.load();
        String DATABASE_URL = "jdbc:" + env.get("DB_CONNECTION") + "://" + env.get("DB_HOST") + ":" + env.get("DB_PORT") + "/" + env.get("DB_DATABASE");

        if (this.connection != null) {
            return this.connection;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(DATABASE_URL, env.get("DB_USERNAME"), env.get("DB_PASSWORD"));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }

        return this.connection;
    }
}
