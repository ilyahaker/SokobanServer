package io.ilyahaker.sokobanserver.database.impl;

public class DatabaseMySqlImpl extends DatabaseImpl {

    protected DatabaseMySqlImpl(String connectionName, String username, String password, String url, int asyncThreads) {
        super(connectionName);
        getPool().setDriverClassName("com.mysql.jdbc.Driver");
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
