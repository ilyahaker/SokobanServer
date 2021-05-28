package io.ilyahaker.sokobanserver.objects;

import com.google.gson.JsonObject;

public interface GameObject {

    JsonObject getItem();

    GameObject getUnderObject();

    void setUnderObject(GameObject object);

}
