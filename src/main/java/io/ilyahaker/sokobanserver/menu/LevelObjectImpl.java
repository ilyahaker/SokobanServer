package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.GameSession;
import io.ilyahaker.sokobanserver.Main;
import io.ilyahaker.sokobanserver.database.api.result.Row;
import io.ilyahaker.sokobanserver.levels.Level;
import io.ilyahaker.sokobanserver.levels.Levels;
import io.ilyahaker.sokobanserver.levels.PassedLevel;
import io.ilyahaker.sokobanserver.objects.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelObjectImpl extends ButtonObjectImpl implements LevelObject {

    @Getter
    private final GamePlayer player;

    private final int levelId;

    public LevelObjectImpl(String name, GamePlayer player, int levelId) {
        super(Material.PAPER, name, new ArrayList<>());
        this.player = player;
        this.levelId = levelId;

        PassedLevel passedLevel = player.getPassedLevel(levelId);
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
        Level currentLevel = Levels.getLevelById(levelId);
        session.setCurrentLevel(currentLevel);
        session.setMatrix(Arrays.stream(currentLevel.getMap())
                .map(gameObjects ->
                        Arrays.stream(gameObjects)
                                .map(object -> object != null ? object.copy() : null)
                                .toArray(GameObject[]::new)
                )
                .toArray(GameObject[][]::new));

        session.setCurrentRow(currentLevel.getStartCurrentRow());
        session.setCurrentColumn(currentLevel.getStartCurrentColumn());

        session.setCountFinish(Arrays.stream(currentLevel.getMap())
                .flatMap(Arrays::stream)
                .filter(object -> object != null && object.getType() == GameObjectType.FINISH)
                .count());

        player.setCoordinateX(currentLevel.getPlayerPosition().getKey());
        player.setCoordinateY(currentLevel.getPlayerPosition().getValue());
        player.setUnderObject(session.getMatrix()[player.getCoordinateX()][player.getCoordinateY()]);
        session.getMatrix()[player.getCoordinateX()][player.getCoordinateY()] = player;

        session.fillInventory();
    }

    @Override
    public GameObject copy() {
        return new LevelObjectImpl(getName(), player, levelId);
    }

}
