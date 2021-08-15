package io.ilyahaker.sokobanserver.objects;

import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class GameObjectImpl implements GameObject {

    @Getter(AccessLevel.PROTECTED)
    private final Material material;

    @Getter
    private final GameObjectType type;

    @Getter
    private final String name;

    public GameObjectImpl(Material material, GameObjectType type, String name) {
        this.material = material;
        this.type = type;
        this.name = name;
    }

    @Override
    public JsonObject getItem() {
        JsonObject item = new JsonObject();
        item.addProperty("material", material.name());
        item.addProperty("name", name);
        return item;
    }
}
