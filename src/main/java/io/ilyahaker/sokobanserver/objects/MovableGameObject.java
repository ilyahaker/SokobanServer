package io.ilyahaker.sokobanserver.objects;

public interface MovableGameObject extends GameObject {

    GameObject getUnderObject();

    void setUnderObject(GameObject object);

}
