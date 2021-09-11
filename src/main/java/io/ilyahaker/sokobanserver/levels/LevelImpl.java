package io.ilyahaker.sokobanserver.levels;

import io.ilyahaker.sokobanserver.objects.BoxObject;
import io.ilyahaker.sokobanserver.objects.FinishObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.MovableGameObject;
import io.ilyahaker.utils.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelImpl implements Level {

    @Getter
    private final GameObject[][] map;

    @Getter
    private final String name;

    @Getter
    private final int id;

    @Getter
    private final Pair<Integer, Integer> playerPosition;

    @Getter
    private final int startCurrentRow;

    @Getter
    private final int startCurrentColumn;

    @Getter
    private final Map<Pair<Integer, Integer>, MovableGameObject> movableGameObjects;

    @Getter
    private final Map<Pair<Integer, Integer>, BoxObject> boxObjects;

    @Getter
    private final Map<Pair<Integer, Integer>, FinishObject> finishObjects;

    private LevelImpl(GameObject[][] map, String name, int id,
                      Pair<Integer, Integer> playerPosition, int startCurrentRow,
                      int startCurrentColumn, Map<Pair<Integer, Integer>, MovableGameObject> movableGameObjects,
                      Map<Pair<Integer, Integer>, BoxObject> boxObjects,
                      Map<Pair<Integer, Integer>, FinishObject> finishObjects) {
        this.map = map;
        this.name = name;
        this.id = id;
        this.playerPosition = playerPosition;
        this.startCurrentRow = startCurrentRow;
        this.startCurrentColumn = startCurrentColumn;
        this.movableGameObjects = movableGameObjects;
        this.boxObjects = boxObjects;
        this.finishObjects = finishObjects;
    }

    public static class LevelBuilder {

        private GameObject[][] map = new GameObject[6][9];
        private String name = "Default Level";
        private Pair<Integer, Integer> playerPosition = new Pair<>(0, 0);
        private int startCurrentRow = 0;
        private int startCurrentColumn = 0;
        private int id;
        private Map<Pair<Integer, Integer>, MovableGameObject> movableGameObjects = new HashMap<>();
        private Map<Pair<Integer, Integer>, BoxObject> boxObjects = new HashMap<>();
        private Map<Pair<Integer, Integer>, FinishObject> finishObjects = new HashMap<>();

        public LevelBuilder map(GameObject[][] map) {
            this.map = map;
            return this;
        }

        public LevelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LevelBuilder id(int id) {
            this.id = id;
            return this;
        }

        public LevelBuilder playerPosition(int positionX, int positionY) {
            this.playerPosition = new Pair<>(positionX, positionY);
            return this;
        }

        public LevelBuilder startCurrentRow(int startCurrentRow) {
            this.startCurrentRow = startCurrentRow;
            return this;
        }

        public LevelBuilder startCurrentColumn(int startCurrentColumn) {
            this.startCurrentColumn = startCurrentColumn;
            return this;
        }

        public LevelBuilder movableGameObjects(Map<Pair<Integer, Integer>, MovableGameObject> movableGameObjects) {
            this.movableGameObjects = movableGameObjects;
            return this;
        }

        public LevelBuilder boxObjects(Map<Pair<Integer, Integer>, BoxObject> boxObjects) {
            this.boxObjects = boxObjects;
            return this;
        }

        public LevelBuilder finishObjects(Map<Pair<Integer, Integer>, FinishObject> finishObjects) {
            this.finishObjects = finishObjects;
            return this;
        }

        public Level build() {
            return new LevelImpl(map, name, id, playerPosition, startCurrentRow, startCurrentColumn, movableGameObjects, boxObjects, finishObjects);
        }

    }
}
