package io.ilyahaker.sokobanserver.database.impl;

import com.zaxxer.hikari.HikariDataSource;
import io.ilyahaker.sokobanserver.database.api.Database;
import io.ilyahaker.sokobanserver.database.api.executor.DatabaseAsyncExecutor;
import io.ilyahaker.sokobanserver.database.api.executor.DatabaseSyncExecutor;
import io.ilyahaker.sokobanserver.database.impl.executor.DatabaseAsyncExecutorImpl;
import io.ilyahaker.sokobanserver.database.impl.executor.DatabaseSyncExecutorImpl;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseImpl implements Database {

    @Getter
    private String connectionName;

    @Getter
    private boolean connected = true;

    private DatabaseSyncExecutorImpl syncExecutor;
    private DatabaseAsyncExecutorImpl asyncExecutor;

    @Getter
    private final HikariDataSource pool = new HikariDataSource();

    protected DatabaseImpl(String connectionName) {
        this.connectionName = connectionName;
    }

    protected void loadDatabase(int asyncThreads) {
        boolean valid;
        try {
            Connection connection = this.pool.getConnection();
            valid = connection.isValid(0);
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            valid = false;
        }

        if (!valid) {
            this.disconnect();
        } else {
            this.syncExecutor = new DatabaseSyncExecutorImpl(this);
            this.asyncExecutor = new DatabaseAsyncExecutorImpl(this, asyncThreads);
        }

    }

    @Override
    public DatabaseAsyncExecutor async() {
        if (!this.connected) {
            throw new IllegalStateException(String.format("The database %s was disconnected!", this.connectionName));
        } else {
            return this.asyncExecutor;
        }
    }

    @Override
    public DatabaseSyncExecutor sync() {
        if (!this.connected) {
            throw new IllegalStateException(String.format("The database %s was disconnected!", this.connectionName));
        } else {
            return this.syncExecutor;
        }
    }

    @Override
    public void disconnect() {
        if (asyncExecutor != null) {
            asyncExecutor.disconnect();
        }

        pool.close();
        this.connected = false;
    }
}
