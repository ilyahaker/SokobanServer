package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.objects.ButtonObject;
import io.ilyahaker.sokobanserver.objects.ButtonObjectImpl;
import io.ilyahaker.sokobanserver.objects.GamePlayer;
import io.ilyahaker.sokobanserver.objects.Material;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SideMenu {

    private final GamePlayer player;

    @Getter
    private final ButtonObject[] buttons;

    private final List<ButtonObject> menu;

    private int page = 0;

    public SideMenu(GamePlayer player) {
        this.player = player;
        buttons = new ButtonObject[6];
        buttons[0] = new ButtonObjectImpl(Material.LIME_STAINED_GLASS_PANE, "Preview items", new ArrayList<>(), session -> {
            previewItems();

            session.fillInventory();
        });
        buttons[5] = new ButtonObjectImpl(Material.LIME_STAINED_GLASS_PANE, "Next items", new ArrayList<>(), session -> {
            nextItems();

            session.fillInventory();
        });

        menu = new ArrayList<>();
    }

    public void removeItems() {
        for (int i = 0; i < Math.min(4, menu.size() - page); i++) {
            buttons[i + 1] = null;
        }
    }

    public void fillItems() {
        for (int i = 0; i < Math.min(4, menu.size() - page); i++) {
            buttons[i + 1] = menu.get(i + page);
        }
    }

    public void previewItems() {
        removeItems();
        page = menu.size() - page > 0 ? page + 1 : page;
        fillItems();
    }

    public void nextItems() {
        removeItems();
        page = page == 0 ? page : page - 1;
        fillItems();
    }
}
