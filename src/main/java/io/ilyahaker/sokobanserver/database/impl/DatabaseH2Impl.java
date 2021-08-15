package io.ilyahaker.sokobanserver.database.impl;

public class DatabaseH2Impl extends DatabaseImpl {

    protected DatabaseH2Impl(String connectionName, String url, int asyncThreads) {
        super(connectionName);
        getPool().setDriverClassName("org.h2.Driver");
        getPool().setJdbcUrl(url);
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
