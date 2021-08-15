package io.ilyahaker.sokobanserver.database.api.executor;

import io.ilyahaker.sokobanserver.database.api.PreparedQuery;
import io.ilyahaker.sokobanserver.database.api.result.SelectResult;

public interface DatabaseSyncExecutor extends DatabaseExecutor {

    SelectResult select(String query);

    PreparedQuery<SelectResult> prepareSelect(String query);

    boolean update(String query);

    PreparedQuery<Boolean> prepareUpdate(String query);

}
