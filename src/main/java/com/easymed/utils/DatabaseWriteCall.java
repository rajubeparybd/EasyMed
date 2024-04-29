package com.easymed.utils;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * DatabaseWriteCall for handling database write operations in a separate thread using JavaFX Task
 *
 * @author Raju Bepary
 * @since 1.0.0
 */
public class DatabaseWriteCall extends Task<Integer> {
    private final String query;
    private final HashMap<Integer, Object> placeholders;
    private PreparedStatement statement;
    private Integer insertedRows = null;

    /**
     * Set the query and placeholders for the database write operation
     *
     * @param query        sql query for database write operation
     * @param placeholders HashMap of placeholders for the query
     */
    public DatabaseWriteCall(String query, HashMap<Integer, Object> placeholders) {
        this.query = query;
        this.placeholders = placeholders;
    }

    /**
     * Prepare the statement with the placeholders
     *
     * @param placeholders HashMap of placeholders for the query
     *
     * @throws SQLException if a database access error occurs
     */
    private void prepareStatement(HashMap<Integer, Object> placeholders) throws SQLException {
        for (int index : placeholders.keySet()) {
            switch (placeholders.get(index)) {
                case String s -> this.statement.setString(index, s);
                case Integer i -> this.statement.setInt(index, (int) placeholders.get(index));
                case Boolean b -> this.statement.setBoolean(index, (boolean) placeholders.get(index));
                case Double v -> this.statement.setDouble(index, (double) placeholders.get(index));
                case null, default -> {
                }
            }
        }
    }

    /**
     * Get the ResultSet from the database write operation
     *
     * @return ResultSet
     */
    public Integer getInsertedRows() {
        try {
            Connection connection = new DB().getConnection();
            this.statement = connection.prepareStatement(query);
            this.prepareStatement(placeholders);
            insertedRows = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("DatabaseWriteCall Error: " + e.getMessage());
        }
        return insertedRows;
    }

    @Override
    protected Integer call() {
        return getInsertedRows();
    }
}
