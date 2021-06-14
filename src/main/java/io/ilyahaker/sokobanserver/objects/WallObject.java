package io.ilyahaker.sokobanserver.objects;

public class WallObject extends GameObjectImpl {

    public WallObject(String material) {
        super(material, GameObjectType.WALL);
    }

}
