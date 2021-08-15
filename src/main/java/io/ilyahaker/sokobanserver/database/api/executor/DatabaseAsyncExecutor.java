package io.ilyahaker.sokobanserver.database.api.executor;

import io.ilyahaker.sokobanserver.database.api.PreparedQuery;
import io.ilyahaker.sokobanserver.database.api.result.SelectResult;

import java.util.concurrent.CompletableFuture;

public interface DatabaseAsyncExecutor {

    CompletableFuture<SelectResult> select(String query);

    PreparedQuery<CompletableFuture<SelectResult>> prepareSelect(String query);

    CompletableFuture<Boolean> update(String query);

    PreparedQuery<CompletableFuture<Boolean>> prepareUpdate(String query);

}
