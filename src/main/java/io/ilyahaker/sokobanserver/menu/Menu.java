package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.FillingStrategy;
import io.ilyahaker.sokobanserver.Level;
import io.ilyahaker.sokobanserver.TestLevel;
import io.ilyahaker.sokobanserver.objects.DecorationObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.Material;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class Menu {

    @Getter
    private final GameObject[][] menu;

    private int page = 0;

    private final Level[] levels;

    public Menu() {
        menu = new FillingStrategy(Map.of('p', new DecorationObject(Material.PURPLE_STAINED_GLASS, ""),
                'r', new DecorationObject(Material.RED_STAINED_GLASS, ""),
                'm', new DecorationObject(Material.MAGENTA_STAINED_GLASS, ""),
                'u', new PageUpObjectImpl(),
                'd', new PageDownObjectImpl()),
                List.of("ppppppppp", "prm___drp", "prm___mrp", "prm___mrp", "prm___urp", "ppppppppp")).getObjects();

        levels = new Level[1];
        levels[0] = new TestLevel();

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
            menu[1 + i / 3 - page][3 + i % 3] = new LevelObjectImpl(levels[i].getName());
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
