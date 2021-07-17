package io.ilyahaker.sokobanserver.objects;

public class WallObject extends GameObjectImpl {

    public WallObject(Material material) {
        super(material, GameObjectType.WALL);
    }

}
