package io.ilyahaker.sokobanserver.objects;

public class WallObject extends GameObjectImpl {

    public WallObject(Material material, String name) {
        super(material, GameObjectType.WALL, name);
    }

}
