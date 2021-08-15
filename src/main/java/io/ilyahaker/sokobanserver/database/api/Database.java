package io.ilyahaker.sokobanserver.database.api;

import io.ilyahaker.sokobanserver.database.api.executor.DatabaseAsyncExecutor;
import io.ilyahaker.sokobanserver.database.api.executor.DatabaseSyncExecutor;
import io.ilyahaker.sokobanserver.database.impl.DatabaseBuilderImpl;

public interface Database {

    DatabaseAsyncExecutor async();

    DatabaseSyncExecutor sync();

    boolean isConnected();

    void disconnect();

    static DatabaseBuilder builder() {
        return new DatabaseBuilderImpl();
    }

    String getConnectionName();
}
