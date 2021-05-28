package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.websocket.Websocket;
import io.ilyahaker.websocket.WebsocketServer;

import java.net.Socket;

public class SokobanServer extends WebsocketServer {
    private GameObject[][] startMatrix;

    public SokobanServer(int port, GameObject[][] startMatrix) {
        super(port);
        this.startMatrix = startMatrix;
    }

    @Override
    public int createWebsocket(Socket clientSocket) {
        SokobanWebsocket websocket = new SokobanWebsocket(clientSocket, startMatrix);
        return websocket.load();
    }
}
