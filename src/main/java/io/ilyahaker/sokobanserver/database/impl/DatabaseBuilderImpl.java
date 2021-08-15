package io.ilyahaker.sokobanserver.database.impl;

import io.ilyahaker.sokobanserver.database.api.Database;
import io.ilyahaker.sokobanserver.database.api.DatabaseBuilder;

public class DatabaseBuilderImpl implements DatabaseBuilder {

    public enum DatabaseType { MYSQL, H2, POSTGRES }

    private String connectionName;
    private String host;
    private int port = 3306;
    private String login;
    private String password;
    private String databaseName;
    private DatabaseType databaseType;
    private boolean useSSL;
    private int asyncThreads = 3;
    private String pathToLocalDb;

    public DatabaseBuilderImpl() {
    }

    @Override
    public DatabaseBuilder connectionName(String name) {
        this.connectionName = name;
        return this;
    }

    @Override
    public DatabaseBuilder host(String host) {
        this.host = host;
        return this;
    }

    @Override
    public DatabaseBuilder port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public DatabaseBuilder login(String login) {
        this.login = login;
        return this;
    }

    @Override
    public DatabaseBuilder password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public DatabaseBuilder databaseName(String name) {
        this.databaseName = name;
        return this;
    }

    @Override
    public DatabaseBuilder databaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
        return this;
    }

    @Override
    public DatabaseBuilder useSSL(boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }

    @Override
    public DatabaseBuilder asyncThreads(int asyncThreads) {
        this.asyncThreads = asyncThreads;
        return this;
    }

    @Override
    public DatabaseBuilder pathToLocalDb(String pathToLocalDb) {
        this.pathToLocalDb = pathToLocalDb;
        return this;
    }

    @Override
    public Database build() {
        StringBuilder url = new StringBuilder();
        switch (this.databaseType) {
            case MYSQL -> {
                url.append("jdbc:mysql://");
                url.append(this.host).append(':').append(this.port);
                url.append('/');
                url.append(this.databaseName);
                url.append("?characterEncoding=utf-8&useUnicode=true");
                if (!useSSL) {
                    url.append("&useSSL=false");
                }
                url.append("&serverTimezone=UTC");
                return new DatabaseMySqlImpl(this.connectionName, this.login, this.password, url.toString(), asyncThreads);
            }
            case H2 -> {
                url.append("jdbc:h2:");
                url.append(pathToLocalDb);
                return new DatabaseH2Impl(this.connectionName, url.toString(), asyncThreads);
            }
            case POSTGRES -> {
                url.append("jdbc:postgresql://")
                        .append(this.host)
                        .append(':')
                        .append(this.port)
                        .append('/')
                        .append(this.databaseName)
                        .append("?characterEncoding=utf-8&useUnicode=true");

                if (!useSSL) {
                    url.append("&useSSL=false");
                }
                url.append("&serverTimezone=UTC");
                return new DatabasePostgresImpl(this.connectionName, this.login, this.password, url.toString(), asyncThreads);
            }
            default -> throw new IllegalStateException("Incorrect database type!");
        }
    }
}
