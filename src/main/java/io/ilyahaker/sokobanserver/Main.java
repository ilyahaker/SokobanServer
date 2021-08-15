package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.levels.Levels;

public class Main {

    public static void main(String[] args) {
        Levels.loadLevels();
        int portNumber = 8003;
        SokobanServer server = new SokobanServer(portNumber);
        server.loadServer();
    }

}
