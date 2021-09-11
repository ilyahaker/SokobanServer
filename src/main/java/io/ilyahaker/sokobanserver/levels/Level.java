package io.ilyahaker.sokobanserver.levels;

import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;

import java.util.Map;

public interface Level {

    GameObject[][] getMap();

    int getId();

    String getName();

    Pair<Integer, Integer> getPlayerPosition();

    int getStartCurrentRow();

    int getStartCurrentColumn();

    Map<Pair<Integer, Integer>, MovableGameObject> getMovableGameObjects();

    Map<Pair<Integer, Integer>, BoxObject> getBoxObjects();

    Map<Pair<Integer, Integer>, FinishObject> getFinishObjects();

}
