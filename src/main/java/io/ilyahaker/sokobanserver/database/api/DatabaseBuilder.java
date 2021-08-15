package io.ilyahaker.sokobanserver.database.api;

import io.ilyahaker.sokobanserver.database.impl.DatabaseBuilderImpl;

public interface DatabaseBuilder {

    DatabaseBuilder connectionName(String name);

    DatabaseBuilder host(String host);

    DatabaseBuilder port(int port);

    DatabaseBuilder login(String login);

    DatabaseBuilder password(String password);

    DatabaseBuilder databaseName(String name);

    DatabaseBuilder databaseType(DatabaseBuilderImpl.DatabaseType databaseType);

    DatabaseBuilder useSSL(boolean useSSL);

    DatabaseBuilder asyncThreads(int asyncThreads);

    DatabaseBuilder pathToLocalDb(String path);

    Database build();

}
