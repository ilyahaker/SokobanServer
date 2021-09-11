package io.ilyahaker.sokobanserver.objects;

import io.ilyahaker.sokobanserver.levels.PassedLevel;

public interface GamePlayer extends MovableGameObject {

    int getId();

    String getName();

    PassedLevel getPassedLevel(int id);

    void putPassedLevel(int id, PassedLevel passedLevel);

}
