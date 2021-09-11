package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillingStrategy {

    @Getter
    private final GameObject[][] objects;

    @Getter
    private final Map<Pair<Integer, Integer>, MovableGameObject> movableGameObjects = new HashMap<>();

    @Getter
    private final Map<Pair<Integer, Integer>, BoxObject> boxObjects = new HashMap<>();

    @Getter
    private final Map<Pair<Integer, Integer>, FinishObject> finishObjects = new HashMap<>();

    public FillingStrategy(Map<Character, GameObject> items, List<String> lines) {
        int length = lines.get(0).length();
        objects = new GameObject[lines.size()][length];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.length() != length) {
                throw new IllegalArgumentException("GUI row must have the same chars");
            }

            for (int j = 0; j < length; j++) {
                char ch = line.charAt(j);

                if (!items.containsKey(ch)) {
                    continue;
                }

                GameObject object = items.get(ch).copy();
                object.setCoordinateX(j);
                object.setCoordinateY(i);

                if (object.getType() == GameObjectType.MOVABLE) {
                    MovableGameObject movableGameObject = (MovableGameObject) object;
                    movableGameObjects.put(new Pair<>(j, i), movableGameObject);
                } else if (object.getType() == GameObjectType.BOX) {
                    BoxObject boxObject = (BoxObject) object;
                    boxObjects.put(new Pair<>(j, i), boxObject);
                } else if (object.getType() == GameObjectType.FINISH) {
                    FinishObject finishObject = (FinishObject) object;
                    finishObjects.put(new Pair<>(j, i), finishObject);
                } else {
                    objects[i][j] = items.get(ch).copy();
                }
            }
        }
    }
}
