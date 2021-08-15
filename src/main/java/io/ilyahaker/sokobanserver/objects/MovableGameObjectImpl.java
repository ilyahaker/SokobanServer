package io.ilyahaker.sokobanserver.objects;

import lombok.Getter;
import lombok.Setter;

public class MovableGameObjectImpl extends GameObjectImpl implements MovableGameObject {

    @Getter
    @Setter
    private GameObject underObject;

    public MovableGameObjectImpl(Material material, String name) {
        super(material, GameObjectType.MOVABLE, name);
    }

    @Override
    public GameObject copy() {
        MovableGameObject movableGameObject = new MovableGameObjectImpl(getMaterial(), getName());
        movableGameObject.setUnderObject(this.underObject);
        return movableGameObject;
    }
}
