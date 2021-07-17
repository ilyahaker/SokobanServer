package io.ilyahaker.sokobanserver.objects;

public class FinishObjectIml extends GameObjectImpl implements FinishObject {

    public FinishObjectIml() {
        super(Material.BARRIER, GameObjectType.FINISH, "Finish");
    }

}
