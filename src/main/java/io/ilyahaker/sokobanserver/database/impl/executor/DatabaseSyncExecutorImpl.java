package io.ilyahaker.sokobanserver.database.impl.executor;

import io.ilyahaker.sokobanserver.database.api.PreparedQuery;
import io.ilyahaker.sokobanserver.database.api.executor.DatabaseSyncExecutor;
import io.ilyahaker.sokobanserver.database.api.result.SelectResult;
import io.ilyahaker.sokobanserver.database.impl.DatabaseImpl;
import io.ilyahaker.sokobanserver.database.impl.PreparedQueryImpl;

import java.sql.SQLException;

public class DatabaseSyncExecutorImpl extends DatabaseExecutorImpl implements DatabaseSyncExecutor {

    public DatabaseSyncExecutorImpl(DatabaseImpl database) {
        super(database);
    }

    @Override
    public SelectResult select(String query) {
        try {
            return this.superSelect(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PreparedQuery<SelectResult> prepareSelect(String query) {
        return new PreparedQueryImpl<>(this::getConnection, query, (statement) -> {
            SelectResult result = wrapResult(statement.executeQuery());
            statement.close();
            return result;
        },
                (value, connection) -> connection.close()
        );
    }

    @Override
    public boolean update(String query) {
        try {
            return this.superUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PreparedQuery<Boolean> prepareUpdate(String query) {
        return new PreparedQueryImpl<>(this::getConnection, query, statement -> {
            boolean result = statement.execute();
            statement.close();
            return result;
        },
                (value, connection) -> connection.close()
        );
    }
}
