package io.ilyahaker.sokobanserver.objects;

import java.util.ArrayList;

public class FinishObjectIml extends GameObjectImpl implements FinishObject {

    public FinishObjectIml() {
        super(Material.BARRIER, GameObjectType.FINISH, "Finish", new ArrayList<>());
    }

    @Override
    public GameObject copy() {
        return new FinishObjectIml();
    }
}
