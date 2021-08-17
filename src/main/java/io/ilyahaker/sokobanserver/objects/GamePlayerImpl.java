package io.ilyahaker.sokobanserver.objects;

import lombok.Getter;
import lombok.Setter;

public class GamePlayerImpl extends MovableGameObjectImpl implements GamePlayer {

    @Getter
    private final int id;

    @Getter
    private final String name;

    @Getter
    @Setter
    private int coordinateX;

    @Getter
    @Setter
    private int coordinateY;

    public GamePlayerImpl(int id, String name) {
        super(Material.PLAYER_HEAD, name);
        this.id = id;
        this.name = name;
    }

    @Override
    public GameObject copy() {
        GamePlayer player = new GamePlayerImpl(id, name);
        player.setCoordinateX(coordinateX);
        player.setCoordinateY(coordinateY);
        player.setUnderObject(getUnderObject().copy());
        return player;
    }
}
