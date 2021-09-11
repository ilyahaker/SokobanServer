package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.GameSession;
import io.ilyahaker.sokobanserver.State;
import io.ilyahaker.sokobanserver.levels.Level;
import io.ilyahaker.sokobanserver.levels.PassedLevel;
import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LevelObjectImpl extends ButtonObjectImpl implements LevelObject {

    @Getter
    private final GamePlayer player;

    private final Level level;

    public LevelObjectImpl(Level level, GamePlayer player) {
        super(Material.PAPER, level.getName(), new ArrayList<>());
        this.player = player;
        this.level = level;
    }

    @Override
    public List<String> getLore() {
        PassedLevel passedLevel = player.getPassedLevel(level.getId());
        List<String> lore = new ArrayList<>(super.getLore());

        if (passedLevel == null) {
            lore.add("You have not passed this level yet.");
        } else {
            lore.add("You had passed this level already.");
            lore.add("Your top result is " + passedLevel.getSteps() + " steps.");
            lore.add("Your last result is " + passedLevel.getLastSteps() + " steps.");
        }

        return lore;
    }

    @Override
    public void click(GameSession session) {
        session.setCurrentLevel(level);
        session.setState(State.GAME);
        GameObject[][] map = Arrays.stream(level.getMap())
                .map(gameObjects ->
                        Arrays.stream(gameObjects)
                                .map(object -> object != null ? object.copy() : null)
                                .toArray(GameObject[]::new)
                )
                .toArray(GameObject[][]::new);

        session.setMap(map);
        session.setCurrentRow(level.getStartCurrentRow());
        session.setCurrentColumn(level.getStartCurrentColumn());

        session.setMovableGameObjects(level.getMovableGameObjects());
        session.setBoxObjects(level.getBoxObjects());
        session.setFinishObjects(level.getFinishObjects());

        session.setCountFinish(level.getFinishObjects().size());

        player.setCoordinateX(level.getPlayerPosition().getKey());
        player.setCoordinateY(level.getPlayerPosition().getValue());
        session.setPlayers(Map.of(new Pair<>(player.getCoordinateX(), player.getCoordinateY()), player));
        player.setUnderObject(map[player.getCoordinateX()][player.getCoordinateY()]);

        session.fillInventory();
    }

    @Override
    public GameObject copy() {
        return new LevelObjectImpl(level, player);
    }

}
