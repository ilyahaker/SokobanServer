package io.ilyahaker.sokobanserver.objects;

import lombok.Getter;
import lombok.Setter;

public class MovableGameObject extends GameObjectImpl {

    @Getter
    @Setter
    private GameObject underObject;

    public MovableGameObject(String material) {
        super(material);
    }
}
