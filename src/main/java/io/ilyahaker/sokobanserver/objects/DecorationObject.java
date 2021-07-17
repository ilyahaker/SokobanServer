package io.ilyahaker.sokobanserver.objects;

public class DecorationObject extends GameObjectImpl {

    public DecorationObject(Material material, String name) {
        super(material, GameObjectType.DECORATION, name);
    }

}
