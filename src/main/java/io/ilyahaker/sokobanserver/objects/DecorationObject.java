package io.ilyahaker.sokobanserver.objects;

public class DecorationObject extends GameObjectImpl {

    public DecorationObject(String material) {
        super(material);
    }

    @Override
    public GameObject getUnderObject() {
        return null;
    }

    @Override
    public void setUnderObject(GameObject object) {

    }
}
