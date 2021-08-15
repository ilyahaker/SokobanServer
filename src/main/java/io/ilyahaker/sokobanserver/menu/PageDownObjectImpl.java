package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.GameObjectImpl;
import io.ilyahaker.sokobanserver.objects.GameObjectType;
import io.ilyahaker.sokobanserver.objects.Material;

public class PageDownObjectImpl extends GameObjectImpl implements PageDownObject {
    public PageDownObjectImpl() {
        super(Material.LIME_STAINED_GLASS_PANE, GameObjectType.PAGE_DOWN, "Page down");
    }

    @Override
    public GameObject copy() {
        return new PageDownObjectImpl();
    }
}
