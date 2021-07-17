package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;

public class TestLevel implements Level {

    @Override
    public GameObject[][] getMap() {
        GameObject[][] objects = new GameObject[7][14];
        objects[1][2] = new BoxObjectImpl();
        objects[1][3] = new DecorationObject(Material.OAK_PLANKS, "");
        objects[0][2] = new DecorationObject(Material.OAK_PLANKS, "");
        objects[2][2] = new DecorationObject(Material.OAK_PLANKS, "");
        objects[0][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[1][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[2][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[3][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[4][6] = new WallObject(Material.STONE_BRICKS, "Wall");
        objects[4][10] = new FinishObjectIml();

        return objects;
    }

    @Override
    public String getName() {
        return "Test Level";
    }

    @Override
    public Pair<Integer, Integer> getPlayerPosition() {
        return new Pair<>(1, 1);
    }

    @Override
    public int getStartCurrentRow() {
        return 0;
    }

    @Override
    public int getStartCurrentColumn() {
        return 0;
    }
}
