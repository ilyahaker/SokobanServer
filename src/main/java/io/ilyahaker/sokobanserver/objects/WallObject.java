package io.ilyahaker.sokobanserver.objects;

import java.util.ArrayList;

public class WallObject extends GameObjectImpl {

    public WallObject(Material material, String name) {
        super(material, GameObjectType.WALL, name, new ArrayList<>());
    }

    @Override
    public GameObject copy() {
        return new WallObject(getMaterial(), getName());
    }
}
