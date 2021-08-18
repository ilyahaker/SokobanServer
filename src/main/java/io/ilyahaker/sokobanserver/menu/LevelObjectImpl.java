package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.Main;
import io.ilyahaker.sokobanserver.database.api.result.Row;
import io.ilyahaker.sokobanserver.levels.PassedLevel;
import io.ilyahaker.sokobanserver.objects.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LevelObjectImpl extends GameObjectImpl implements LevelObject {

    @Getter
    private final GamePlayer player;

    private final int levelId;

    public LevelObjectImpl(String name, GamePlayer player, int levelId) {
        super(Material.PAPER, GameObjectType.CHOOSE_LEVEL, name, new ArrayList<>());
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
    public GameObject copy() {
        return new LevelObjectImpl(getName(), player, levelId);
    }

}
