package io.ilyahaker.sokobanserver;

public class Main {

    public static void main(String[] args) {
        int portNumber = 8003;
        SokobanServer server = new SokobanServer(portNumber);
        server.loadServer();
    }

}
