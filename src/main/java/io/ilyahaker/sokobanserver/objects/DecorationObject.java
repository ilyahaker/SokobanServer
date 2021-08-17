package io.ilyahaker.sokobanserver.objects;

import java.util.ArrayList;
import java.util.List;

public class DecorationObject extends GameObjectImpl {

    public DecorationObject(Material material, String name, List<String> lore) {
        super(material, GameObjectType.DECORATION, name, lore);
    }

    @Override
    public GameObject copy() {
        return new DecorationObject(getMaterial(), getName(), getLore());
    }
}
