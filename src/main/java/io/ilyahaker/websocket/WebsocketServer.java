package io.ilyahaker.websocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class WebsocketServer {

    private final int port;

    public WebsocketServer(int port) {
        this.port = port;
    }

    protected int createWebsocket(Socket clientSocket) {
        Websocket websocket = new Websocket(clientSocket);
        return websocket.load();
    }

    public void loadServer() {
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not create web server", exception);
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool(1000, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false,
                0, 0x7fff, 1, null, 5L, TimeUnit.MINUTES);
        while (true) {
            try {
                Socket clientSocket = server.accept(); //waits until a client connects

                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> createWebsocket(clientSocket), forkJoinPool);

                future.whenComplete(((code, throwable) -> {
                    System.out.println("Websocket has been closed by code: " + code);
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                }));
            } catch (IOException waitException) {
                throw new IllegalStateException("Could not wait for client connection", waitException);
            }
        }
    }
}
