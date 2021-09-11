package io.ilyahaker.sokobanserver.objects;

import io.ilyahaker.sokobanserver.GameSession;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class MovableGameObjectImpl extends GameObjectImpl implements MovableGameObject {

    @Getter
    @Setter
    private GameObject underObject;

    public MovableGameObjectImpl(Material material, String name) {
        super(material, GameObjectType.MOVABLE, name, new ArrayList<>());
    }

    @Override
    public boolean move(GameSession.Direction direction, GameSession session) {
        switch (direction) {
            case UP -> {

            }
        }
        return false;
    }

    @Override
    public GameObject copy() {
        MovableGameObject movableGameObject = new MovableGameObjectImpl(getMaterial(), getName());
        movableGameObject.setCoordinateX(getCoordinateX());
        movableGameObject.setCoordinateY(getCoordinateY());
        movableGameObject.setUnderObject(this.underObject);
        return movableGameObject;
    }
}
