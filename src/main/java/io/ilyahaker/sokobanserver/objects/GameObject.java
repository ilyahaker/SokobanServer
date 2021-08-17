package io.ilyahaker.sokobanserver.objects;

import com.google.gson.JsonObject;

import java.util.List;

public interface GameObject {

    JsonObject getItem();

    GameObjectType getType();

    String getName();

    GameObject copy();

    List<String> getLore();
}
