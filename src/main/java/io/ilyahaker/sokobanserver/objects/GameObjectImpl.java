package io.ilyahaker.sokobanserver.objects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import lombok.AccessLevel;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class GameObjectImpl implements GameObject {

    @Getter(AccessLevel.PROTECTED)
    private final Material material;

    @Getter
    private final GameObjectType type;

    @Getter
    private final String name;

    @Getter
    private final List<String> lore;

    public GameObjectImpl(Material material, GameObjectType type, String name, List<String> lore) {
        this.material = material;
        this.type = type;
        this.name = name;
        this.lore = lore;
    }

    @Override
    public JsonObject getItem() {
        JsonObject item = new JsonObject();
        item.addProperty("material", material.name());
        item.addProperty("name", name);
        item.add("lore", new Gson().toJsonTree(lore));
        return item;
    }
}
