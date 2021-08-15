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
        super(Material.PLAYER_HEAD, "Player");
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    @Override
    public GameObject copy() {
        GamePlayer player = new GamePlayerImpl(coordinateX, coordinateY);
        player.setUnderObject(getUnderObject());
        return player;
    }
}
