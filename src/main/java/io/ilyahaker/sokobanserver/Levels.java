package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.*;

public enum Levels {
    LEVEL_1;

    public GameObject[][] getStart() {
        GameObject[][] objects = new GameObject[7][14];
        objects[1][2] = new BoxObjectImpl();
        objects[1][3] = new DecorationObject(Material.OAK_PLANKS);
        objects[0][2] = new DecorationObject(Material.OAK_PLANKS);
        objects[2][2] = new DecorationObject(Material.OAK_PLANKS);
        objects[0][6] = new WallObject(Material.STONE_BRICKS);
        objects[1][6] = new WallObject(Material.STONE_BRICKS);
        objects[2][6] = new WallObject(Material.STONE_BRICKS);
        objects[3][6] = new WallObject(Material.STONE_BRICKS);
        objects[4][6] = new WallObject(Material.STONE_BRICKS);
        objects[4][10] = new FinishObjectIml();

        return objects;
    }
}
