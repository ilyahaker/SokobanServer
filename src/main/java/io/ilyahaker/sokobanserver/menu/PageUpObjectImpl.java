package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.objects.GameObjectImpl;
import io.ilyahaker.sokobanserver.objects.GameObjectType;
import io.ilyahaker.sokobanserver.objects.Material;

public class PageUpObjectImpl extends GameObjectImpl implements PageUpObject {
    public PageUpObjectImpl() {
        super(Material.LIME_STAINED_GLASS_PANE, GameObjectType.PAGE_UP, "Page up");
    }
}
