package io.ilyahaker.sokobanserver.objects;

import com.google.gson.JsonObject;
import lombok.Getter;

public abstract class GameObjectImpl implements GameObject {

    private final String material;

    @Getter
    private final GameObjectType type;

    public GameObjectImpl(String material, GameObjectType type) {
        this.material = material;
        this.type = type;
    }

    @Override
    public JsonObject getItem() {
        JsonObject item = new JsonObject();
        item.addProperty("material", material);
        return item;
    }
}
