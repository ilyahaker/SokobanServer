package io.ilyahaker.sokobanserver.objects;

import io.ilyahaker.sokobanserver.levels.PassedLevel;

public interface GamePlayer extends MovableGameObject {

    int getId();

    String getName();

    int getCoordinateX();

    int getCoordinateY();

    void setCoordinateX(int coordinateX);

    void setCoordinateY(int coordinateY);

    PassedLevel getPassedLevel(int id);

}
