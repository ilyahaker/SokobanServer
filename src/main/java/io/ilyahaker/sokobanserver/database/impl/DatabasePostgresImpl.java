package io.ilyahaker.sokobanserver.database.impl;

public class DatabasePostgresImpl extends DatabaseImpl {

    protected DatabasePostgresImpl(String connectionName, String username, String password, String url, int asyncThreads) {
        super(connectionName);
        getPool().setDriverClassName("org.postgresql.Driver");
        getPool().setJdbcUrl(url);
        getPool().setUsername(username);
        getPool().setPassword(password);
        getPool().setMaximumPoolSize(asyncThreads);
        getPool().setMinimumIdle(0);
        getPool().setIdleTimeout(30000L);
        getPool().setMaxLifetime(60000L);
        getPool().setPoolName("HikariPool#" + connectionName);
        getPool().addDataSourceProperty("cachePrepStmts", "true");
        getPool().addDataSourceProperty("prepStmtCacheSize", "250");
        getPool().addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        getPool().addDataSourceProperty("useServerPrepStmts", true);
        getPool().addDataSourceProperty("cacheResultSetMetadata", true);

        loadDatabase(asyncThreads);
    }

}
