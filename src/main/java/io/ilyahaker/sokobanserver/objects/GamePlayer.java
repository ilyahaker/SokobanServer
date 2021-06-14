package io.ilyahaker.sokobanserver.objects;

public interface GamePlayer extends MovableGameObject {

    int getCoordinateX();

    int getCoordinateY();

    void setCoordinateX(int coordinateX);

    void setCoordinateY(int coordinateY);

}
