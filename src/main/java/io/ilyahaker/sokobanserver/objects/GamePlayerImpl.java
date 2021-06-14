package io.ilyahaker.sokobanserver.objects;

import lombok.Getter;
import lombok.Setter;

public class GamePlayerImpl extends MovableGameObjectImpl implements GamePlayer {

    @Getter
    @Setter
    private int coordinateX;

    @Getter
    @Setter
    private int coordinateY;

    public GamePlayerImpl(int coordinateX, int coordinateY) {
        super(Material.PLAYER_HEAD.name());
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }
}
