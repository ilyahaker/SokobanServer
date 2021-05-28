package io.ilyahaker.sokobanserver.objects;

import com.google.gson.JsonObject;

public abstract class GameObjectImpl implements GameObject {

    private final String material;

    public GameObjectImpl(String material) {
        this.material = material;
    }

    @Override
    public JsonObject getItem() {
        JsonObject item = new JsonObject();
        item.addProperty("material", material);
        return item;
    }
}
