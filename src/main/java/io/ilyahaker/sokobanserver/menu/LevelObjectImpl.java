package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.GameSession;
import io.ilyahaker.sokobanserver.levels.Level;
import io.ilyahaker.sokobanserver.levels.PassedLevel;
import io.ilyahaker.sokobanserver.objects.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelObjectImpl extends ButtonObjectImpl implements LevelObject {

    @Getter
    private final GamePlayer player;

    private final Level level;

    public LevelObjectImpl(Level level, GamePlayer player) {
        super(Material.PAPER, level.getName(), new ArrayList<>());
        this.player = player;
        this.level = level;

        PassedLevel passedLevel = player.getPassedLevel(level.getId());
        if (passedLevel == null) {
            getLore().add("You have not passed this level yet.");
        } else {
            getLore().add("You had passed this level already.");
            getLore().add("Your top result is " + passedLevel.getSteps() + " steps.");
            getLore().add("Your last result is " + passedLevel.getLastSteps() + " steps.");
        }
    }

    @Override
    public void click(GameSession session) {
        session.setCurrentLevel(level);
        session.setMatrix(Arrays.stream(level.getMap())
                .map(gameObjects ->
                        Arrays.stream(gameObjects)
                                .map(object -> object != null ? object.copy() : null)
                                .toArray(GameObject[]::new)
                )
                .toArray(GameObject[][]::new));

        session.setCurrentRow(level.getStartCurrentRow());
        session.setCurrentColumn(level.getStartCurrentColumn());

        session.setCountFinish(Arrays.stream(level.getMap())
                .flatMap(Arrays::stream)
                .filter(object -> object != null && object.getType() == GameObjectType.FINISH)
                .count());

        player.setCoordinateX(level.getPlayerPosition().getKey());
        player.setCoordinateY(level.getPlayerPosition().getValue());
        player.setUnderObject(session.getMatrix()[player.getCoordinateX()][player.getCoordinateY()]);
        session.getMatrix()[player.getCoordinateX()][player.getCoordinateY()] = player;

        session.fillInventory();
    }

    @Override
    public GameObject copy() {
        return new LevelObjectImpl(level, player);
    }

}
