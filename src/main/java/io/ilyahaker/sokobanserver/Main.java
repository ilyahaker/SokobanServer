package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.database.api.Database;
import io.ilyahaker.sokobanserver.database.api.result.SelectResult;
import io.ilyahaker.sokobanserver.database.impl.DatabaseBuilderImpl;
import io.ilyahaker.sokobanserver.levels.Levels;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    @Getter
    private static Database database;

    public static void main(String[] args) {
        File configFile = new File(Paths.get("").toAbsolutePath().toString(), "config.yml");
        if (configFile.exists()) {
            try {
                InputStream inputStream = new FileInputStream(configFile);
                Yaml yaml = new Yaml();
                Map<String, Object> config = yaml.load(inputStream);
                String host = (String) config.get("host");
                String login = (String) config.get("login");
                String password = (String) config.get("password");
                int port = (Integer) config.get("port");
                String databaseName = (String) config.get("databaseName");
                boolean useSSL = (boolean) config.get("useSSL");

                database = Database.builder()
                        .host(host)
                        .login(login)
                        .password(password)
                        .port(port)
                        .databaseName(databaseName)
                        .databaseType(DatabaseBuilderImpl.DatabaseType.POSTGRES)
                        .useSSL(useSSL)
                        .build();


                database.async().update("""
                    create table if not exists sokoban_users (
                    id serial,
                    login text not null,
                    password text not null,
                    primary key (id));
                """);

                database.async().update("""
                    create table if not exists sokoban_passed_levels (
                    player_id int,
                    level_id int,
                    steps int default 0,
                    last_steps int default 0,
                    primary key (player_id, level_id));
                """);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Levels.loadLevels();
        int portNumber = 8003;
        SokobanServer server = new SokobanServer(portNumber);
        server.loadServer();
    }

}
