package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.DecorationObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.Material;
import io.ilyahaker.websocket.WebsocketServer;

public class Main {

    public static void main(String[] args) {
        int portNumber = 8003;
        GameObject[][] objects = new GameObject[3][3];
        objects[1][1] = new DecorationObject(Material.OAK_PLANKS.name());
        objects[0][2] = new DecorationObject(Material.OAK_PLANKS.name());
        objects[2][2] = new DecorationObject(Material.OAK_PLANKS.name());

        SokobanServer server = new SokobanServer(portNumber, objects);
        server.loadServer();
    }

}
