package io.ilyahaker.sokobanserver.levels;

import io.ilyahaker.sokobanserver.FillingStrategy;
import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Levels {

    @Getter
    private static List<Level> levelList = new ArrayList<>();

    private static Map<Integer, Level> levelById = new HashMap<>();

    /*
    File could consist level name,
    level dimension, characters and game objects,
    list of strings is consisting the level map.
    */
    private static boolean loadYamlLevels() {
        File levels = new File(Paths.get("").toAbsolutePath().toString(), "levels");
        if (!levels.exists() || !levels.isDirectory()) {
            System.out.println("ERROR");
            return false;
        }

        try {
            for (File file : levels.listFiles()) {
                System.out.println(file.toString());
                InputStream inputStream = new FileInputStream(file);
                Yaml yaml = new Yaml();
                Map<String, Object> obj = yaml.load(inputStream);
                int id = (Integer) obj.get("id");
                String name = (String) obj.get("name");
                int playerPositionX = (Integer) obj.getOrDefault("playerPositionX", 1);
                int playerPositionY = (Integer) obj.getOrDefault("playerPositionY", 1);

                Map<Character, GameObject> objects = ((Map<String, Object>) obj.get("objects")).entrySet()
                        .stream()
                        .collect(Collectors.toMap(entry -> entry.getKey().charAt(0), entry -> {
                            Map<String, Object> object = ((Map<String, Object>) entry.getValue());
                            return switch (GameObjectType.valueOf((String) object.get("type"))) {
                                case BOX -> new BoxObjectImpl();
                                case FINISH -> new FinishObjectIml();
                                case DECORATION ->
                                        new DecorationObject(Material.valueOf((String) object.get("material")), (String) object.get("name"),
                                                new ArrayList<>());
                                case WALL ->
                                        new WallObject(Material.valueOf((String) object.get("material")), (String) object.get("name"));
                                default -> null;
                            };
                        }));

                List<String> map = (List<String>) obj.get("map");

                int row = 0;
                if (map.size() - playerPositionY >= 4 && playerPositionY >= 2) {
                     row = Math.max(playerPositionY - 2, 0);
                }

                int column = 0;
                if (map.get(0).length() - playerPositionX >= 5 && playerPositionX >= 4) {
                    column = Math.max(playerPositionX - 4, 0);
                }

                FillingStrategy fillingStrategy = new FillingStrategy(objects, map);
                Level level = new LevelImpl.LevelBuilder()
                        .id(id)
                        .name(name)
                        .map(fillingStrategy.getObjects())
                        .movableGameObjects(fillingStrategy.getMovableGameObjects())
                        .boxObjects(fillingStrategy.getBoxObjects())
                        .finishObjects(fillingStrategy.getFinishObjects())
                        .playerPosition(playerPositionX, playerPositionY)
                        .startCurrentRow(row)
                        .startCurrentColumn(column)
                        .build();

                levelList.add(level);
                levelById.put(id, level);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    private static void loadTestLevels() {
        GameObject[][] objects = new GameObject[7][14];
        Map<Pair<Integer, Integer>, BoxObject> boxObjects = Map.of(new Pair<>(2, 1), new BoxObjectImpl());
        Map<Pair<Integer, Integer>, FinishObject> finishObjects = Map.of(new Pair<>(10, 4), new FinishObjectIml());
//        objects[1][2] = new BoxObjectImpl();
        objects[1][3] = new DecorationObject(Material.OAK_PLANKS, " ", new ArrayList<>());
        objects[0][2] = new DecorationObject(Material.OAK_PLANKS, " ", new ArrayList<>());
        objects[2][2] = new DecorationObject(Material.OAK_PLANKS, " ", new ArrayList<>());
        objects[0][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[1][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[2][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[3][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[4][6] = new WallObject(Material.STONE_BRICKS, "Wall");
//        objects[4][10] = new FinishObjectIml();

        int id = levelList.size();
        Level level = new LevelImpl.LevelBuilder()
                .id(id)
                .map(objects)
                .name("Test Level")
                .playerPosition(1, 1)
                .boxObjects(boxObjects)
                .finishObjects(finishObjects)
                .build();

        levelList.add(level);
        levelById.put(id, level);
    }

    public static void loadLevels() {
        loadTestLevels();
        loadYamlLevels();
    }

    public static Level getLevelById(int id) {
        return levelById.get(id);
    }

}
