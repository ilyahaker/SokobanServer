package io.ilyahaker.sokobanserver.menu;

import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.GameObjectImpl;
import io.ilyahaker.sokobanserver.objects.GameObjectType;
import io.ilyahaker.sokobanserver.objects.Material;

public class LevelObjectImpl extends GameObjectImpl implements LevelObject {

    public LevelObjectImpl(String name) {
        super(Material.PAPER, GameObjectType.CHOOSE_LEVEL, name);
    }

    @Override
    public GameObject copy() {
        return new LevelObjectImpl(getName());
    }
}
