package io.ilyahaker.sokobanserver.objects;

import io.ilyahaker.sokobanserver.GameSession;

public interface ButtonObject extends GameObject {

    void click(GameSession session);

}
