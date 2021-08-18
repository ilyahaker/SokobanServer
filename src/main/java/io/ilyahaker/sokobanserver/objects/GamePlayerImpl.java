package io.ilyahaker.sokobanserver.objects;

import io.ilyahaker.sokobanserver.Main;
import io.ilyahaker.sokobanserver.database.api.result.Row;
import io.ilyahaker.sokobanserver.levels.Level;
import io.ilyahaker.sokobanserver.levels.Levels;
import io.ilyahaker.sokobanserver.levels.PassedLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Getter
    private Map<Integer, PassedLevel> passedLevels = new HashMap<>();

    public GamePlayerImpl(int id, String name) {
        super(Material.PLAYER_HEAD, name);
        this.id = id;
        this.name = name;

        List<Row> rows = Main.getDatabase().sync()
                .prepareSelect("""
                        select * from sokoban_passed_levels where player_id = ?;
                        """).execute(id).getRows();

        for (Row row : rows) {
            passedLevels.put(row.getInt("level_id"),
                    new PassedLevel(Levels.getLevelById(row.getInt("level_id")),
                    row.getInt("steps"), row.getInt("last_steps")));
        }
    }

    @Override
    public GameObject copy() {
        GamePlayer player = new GamePlayerImpl(id, name);
        player.setCoordinateX(coordinateX);
        player.setCoordinateY(coordinateY);
        player.setUnderObject(getUnderObject().copy());
        return player;
    }

    @Override
    public PassedLevel getPassedLevel(int id) {
        return passedLevels.get(id);
    }
}
