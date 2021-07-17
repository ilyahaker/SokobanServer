package io.ilyahaker.sokobanserver.objects;

import com.google.gson.JsonObject;

public interface GameObject {

    JsonObject getItem();

    GameObjectType getType();

    String getName();

}
