package io.ilyahaker.sokobanserver.levels;

import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.utils.Pair;
import lombok.Getter;

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

    private LevelImpl(GameObject[][] map, String name, int id, Pair<Integer, Integer> playerPosition, int startCurrentRow, int startCurrentColumn) {
        this.map = map;
        this.name = name;
        this.id = id;
        this.playerPosition = playerPosition;
        this.startCurrentRow = startCurrentRow;
        this.startCurrentColumn = startCurrentColumn;
    }

    public static class LevelBuilder {

        private GameObject[][] map = new GameObject[6][9];
        private String name = "Default Level";
        private Pair<Integer, Integer> playerPosition = new Pair<>(0, 0);
        private int startCurrentRow = 0;
        private int startCurrentColumn = 0;
        private int id;

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

        public Level build() {
            return new LevelImpl(map, name, id, playerPosition, startCurrentRow, startCurrentColumn);
        }

    }
}
