package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.DecorationObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.websocket.WebsocketServer;

public class Main {

    public static void main(String[] args) {
        int portNumber = 8003;
        GameObject[][] objects = new GameObject[3][3];
        objects[1][1] = new DecorationObject("OAK_WOOD");
        objects[0][2] = new DecorationObject("OAK_WOOD");
        objects[2][2] = new DecorationObject("OAK_WOOD");

        SokobanServer server = new SokobanServer(portNumber, objects);
        server.loadServer();
    }

}
