package io.ilyahaker.sokobanserver.objects;

public class BoxObjectImpl extends MovableGameObjectImpl implements BoxObject {

    public BoxObjectImpl() {
        super(Material.WHITE_SHULKER_BOX, "Box");
    }

    @Override
    public GameObjectType getType() {
        return GameObjectType.BOX;
    }
}
