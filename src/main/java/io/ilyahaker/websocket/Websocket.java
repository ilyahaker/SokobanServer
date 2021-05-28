package io.ilyahaker.websocket;

import java.net.Socket;

public class Websocket extends AbstractWebsocket {

    public Websocket(Socket clientSocket) {
        super(clientSocket);
    }

    @Override
    protected void onText(String message) {
        System.out.println(message);
    }

    @Override
    protected void onPong(String message) {

    }

    @Override
    protected void onPing(String message) {

    }

    @Override
    protected void onClose(String message) {
        System.out.println("onClose message is " + message);
    }

    @Override
    protected void onBinary() {

    }

    @Override
    protected void onStart() {
        sendText("Hello from server!");
    }
}
