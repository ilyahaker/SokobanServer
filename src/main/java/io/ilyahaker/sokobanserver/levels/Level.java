package io.ilyahaker.sokobanserver.levels;

import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;

public interface Level {

    GameObject[][] getMap();

    int getId();

    String getName();

    Pair<Integer, Integer> getPlayerPosition();

    int getStartCurrentRow();

    int getStartCurrentColumn();
}
