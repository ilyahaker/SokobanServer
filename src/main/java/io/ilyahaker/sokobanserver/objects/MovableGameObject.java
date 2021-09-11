package io.ilyahaker.sokobanserver.objects;

import io.ilyahaker.sokobanserver.GameSession;

public interface MovableGameObject extends GameObject {

    GameObject getUnderObject();

    void setUnderObject(GameObject object);

    boolean move(GameSession.Direction direction, GameSession session);
}
