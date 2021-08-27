package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.FillingStrategy;
import io.ilyahaker.sokobanserver.levels.Level;
import io.ilyahaker.sokobanserver.levels.Levels;
import io.ilyahaker.sokobanserver.objects.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Menu {

    private final GamePlayer player;

    @Getter
    private final GameObject[][] menu;

    private int page = 0;

    private final Level[] levels;

    public Menu(GamePlayer player) {
        menu = new FillingStrategy(Map.of('p', new DecorationObject(Material.PURPLE_STAINED_GLASS, " ", new ArrayList<>()),
                'r', new DecorationObject(Material.RED_STAINED_GLASS, " ", new ArrayList<>()),
                'm', new DecorationObject(Material.MAGENTA_STAINED_GLASS, " ", new ArrayList<>()),
                'u', new ButtonObjectImpl(Material.LIME_STAINED_GLASS_PANE, "Page up", new ArrayList<>(), session -> {
                    pageUp();
                    session.setCurrentMenu(getMenu());

                    session.fillInventory();
                }),
                'd', new ButtonObjectImpl(Material.LIME_STAINED_GLASS_PANE, "Page down", new ArrayList<>(), session -> {
                    pageDown();
                    session.setCurrentMenu(getMenu());

                    session.fillInventory();
                })),
                List.of("ppppppppp", "prm___drp", "prm___mrp", "prm___mrp", "prm___urp", "ppppppppp")).getObjects();

        levels = Levels.getLevelList().toArray(new Level[]{});
        this.player = player;

        fillLevels();
    }

    public Level chooseLevel(int row, int column) {
        return levels[(row - 1) * 3 + page + column - 3];
    }

    public void removeLevelsFromMenu() {
        for (int i = 1; i < 5; i++) {
            for (int j = 3; j < 6; j++) {
                menu[i][j] = null;
            }
        }
    }

    public void fillLevels() {
        for (int i = page * 3; i < levels.length; i++) {
            menu[1 + i / 3 - page][3 + i % 3] = new LevelObjectImpl(levels[i], player);
        }
    }

    public void pageUp() {
        removeLevelsFromMenu();
        page++;
        fillLevels();
    }

    public void pageDown() {
        removeLevelsFromMenu();
        page = page == 0 ? page : page - 1;
        fillLevels();
    }
}
