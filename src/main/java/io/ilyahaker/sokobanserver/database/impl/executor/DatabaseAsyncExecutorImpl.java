package io.ilyahaker.sokobanserver.database.impl.executor;

import io.ilyahaker.sokobanserver.database.api.PreparedQuery;
import io.ilyahaker.sokobanserver.database.api.executor.DatabaseAsyncExecutor;
import io.ilyahaker.sokobanserver.database.api.result.SelectResult;
import io.ilyahaker.sokobanserver.database.impl.DatabaseImpl;
import io.ilyahaker.sokobanserver.database.impl.PreparedQueryImpl;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseAsyncExecutorImpl extends DatabaseExecutorImpl implements DatabaseAsyncExecutor {
    private final ExecutorService executor;

    public DatabaseAsyncExecutorImpl(DatabaseImpl database, int threads) {
        super(database);
        this.executor = Executors.newFixedThreadPool(threads, new ThreadFactory() {
            private final AtomicInteger id = new AtomicInteger();

            public Thread newThread(Runnable r) {
                return new Thread(r, "DatabaseThread#" + this.id.incrementAndGet());
            }
        });
    }

    public void disconnect() {
        this.executor.shutdown();
    }

    @Override
    public CompletableFuture<SelectResult> select(String query) {
        CompletableFuture<SelectResult> future = new CompletableFuture<>();
        this.executor.execute(() -> {
            try {
                SelectResult result = this.superSelect(query);
                future.complete(result);
            } catch (SQLException exception) {
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    @Override
    public PreparedQuery<CompletableFuture<SelectResult>> prepareSelect(String query) {
        return new PreparedQueryImpl<>(this::getConnection, query, (statement) ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        SelectResult result = wrapResult(statement.executeQuery());
                        statement.close();
                        return result;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                }, executor),
                (future, connection) -> future.thenAccept((value) -> {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public CompletableFuture<Boolean> update(String query) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.executor.execute(() -> {
            try {
                boolean result = this.superUpdate(query);
                future.complete(result);
            } catch (SQLException var36) {
                future.completeExceptionally(var36);
            }
        });
        return future;
    }

    @Override
    public PreparedQuery<CompletableFuture<Boolean>> prepareUpdate(String query) {
        return new PreparedQueryImpl<>(this::getConnection, query, (statement) ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        boolean result = statement.execute();
                        statement.close();
                        return result;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                }, executor),
                (future, connection) -> future.thenAccept((value) -> {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));
    }
}
