package io.ilyahaker.sokobanserver;

import io.ilyahaker.websocket.WebsocketServer;

public class Main {

    public static void main(String[] args) {
        int portNumber = 8003;
        WebsocketServer server = new WebsocketServer(portNumber);
        server.loadServer();
    }

}
