package io.ilyahaker.sokobanserver.objects;

import lombok.Getter;
import lombok.Setter;

public class MovableGameObjectImpl extends GameObjectImpl implements MovableGameObject {

    @Getter
    @Setter
    private GameObject underObject;

    public MovableGameObjectImpl(String material) {
        super(material, GameObjectType.MOVABLE);
    }
}
