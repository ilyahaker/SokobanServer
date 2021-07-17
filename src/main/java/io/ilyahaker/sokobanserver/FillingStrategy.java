package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.GameObject;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class FillingStrategy {

    @Getter
    private final GameObject[][] objects;

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

                objects[i][j] = items.get(ch);
            }
        }
    }
}
