package io.ilyahaker.sokobanserver.database.impl.executor;

import io.ilyahaker.sokobanserver.database.api.executor.DatabaseExecutor;
import io.ilyahaker.sokobanserver.database.api.result.Column;
import io.ilyahaker.sokobanserver.database.api.result.Field;
import io.ilyahaker.sokobanserver.database.api.result.Row;
import io.ilyahaker.sokobanserver.database.api.result.SelectResult;
import io.ilyahaker.sokobanserver.database.impl.DatabaseImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseExecutorImpl implements DatabaseExecutor {

    //TODO WeakReference????
    private DatabaseImpl database;

    protected DatabaseExecutorImpl(DatabaseImpl database) {
        this.database = database;
    }

    @Override
    public DatabaseImpl getDatabase() {
        return database;
    }

    protected Connection getConnection() {
        if (database == null)
            return null;

        try {
            return database.getPool().getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    protected SelectResult wrapResult(ResultSet resultSet) throws SQLException {
        SelectResult selectResult = new SelectResult();
        ResultSetMetaData resultMetaData = resultSet.getMetaData();
        int columnCount = resultMetaData.getColumnCount();

        List<Column> columns = new ArrayList<>();
        for (int i = 1; i < columnCount + 1; i++) {
            columns.add(new Column(resultMetaData.getColumnName(i)));
        }

        selectResult.setColumns(columns);

        List<Row> rows = new ArrayList<>();
        while (resultSet.next()) {
            List<Field> fields = new ArrayList<>();
            Map<String, Field> fieldMap = new HashMap<>();
            for (int i = 1; i < columnCount + 1; i++) {
                Object object = resultSet.getObject(i);
                Field field = new Field(object.getClass(), object);
                fields.add(field);
                fieldMap.put(columns.get(i - 1).getName(), field);
            }

            Row row = new Row(fieldMap, fields);
            rows.add(row);
        }

        selectResult.setRows(rows);
        resultSet.close();
        return selectResult;
    }

    protected SelectResult superSelect(String query) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        SelectResult result = wrapResult(statement.executeQuery(query));
        statement.close();
        connection.close();
        return result;
    }

    protected boolean superUpdate(String query) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        boolean result = statement.execute(query);
        statement.close();
        connection.close();
        return result;
    }
}
