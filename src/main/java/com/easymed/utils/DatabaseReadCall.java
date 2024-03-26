package com.easymed.utils;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * DatabaseReadCall for handling database read operations in a separate thread using JavaFX Task
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class DatabaseReadCall extends Task<ResultSet> {
    private final String query;
    private final HashMap<Integer, Object> placeholders;
    private PreparedStatement statement;
    private ResultSet resultSet = null;

    /**
     * Set the query and placeholders for the database read operation
     *
     * @param query        sql query for database read operation
     * @param placeholders HashMap of placeholders for the query
     */
    public DatabaseReadCall(String query, HashMap<Integer, Object> placeholders) {
        this.query = query;
        this.placeholders = placeholders;
    }

    private void prepareStatement(HashMap<Integer, Object> placeholders) throws SQLException {
        for (int index : placeholders.keySet()) {
            if (placeholders.get(index) instanceof String) {
                this.statement.setString(index, (String) placeholders.get(index));
            } else if (placeholders.get(index) instanceof Integer) {
                this.statement.setInt(index, (int) placeholders.get(index));
            } else if (placeholders.get(index) instanceof Boolean) {
                this.statement.setBoolean(index, (boolean) placeholders.get(index));
            } else if (placeholders.get(index) instanceof Double) {
                this.statement.setDouble(index, (double) placeholders.get(index));
            }
        }
    }

    /**
     * Get the ResultSet from the database read operation
     *
     * @return ResultSet
     */
    public ResultSet getResultSet() {
        try {
            Connection connection = new DB().getConnection();
            this.statement = connection.prepareStatement(query);
            this.prepareStatement(placeholders);
            resultSet = statement.executeQuery();
        } catch (Exception e) {
            System.out.println("DatabaseReadCall Error: " + e.getMessage());
        }
        return resultSet;
    }

    @Override
    protected ResultSet call() {
        return getResultSet();
    }
}
