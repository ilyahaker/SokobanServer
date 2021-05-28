package io.ilyahaker.sokobanserver;

import com.google.gson.JsonObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.websocket.Websocket;

import java.net.Socket;

public class SokobanWebsocket extends Websocket {
    private GameObject[][] startMatrix;
    private GameSession session;

    public SokobanWebsocket(Socket clientSocket, GameObject[][] startMatrix) {
        super(clientSocket);
        this.startMatrix = startMatrix;
    }

    @Override
    protected void onStart() {
        this.session = new GameSession(startMatrix, this);
    }

    public void sendInventory(GameObject[][] matrix, int currentRow, int currentColumn) {
        JsonObject inventory = new JsonObject();
        for (int row = 0; row < 6; row++) {
            boolean empty = true;
            JsonObject jsonRow = new JsonObject();
            for (int column = 0; column < 9; column++) {
                JsonObject object = getItem(matrix, row + currentRow, column + currentColumn);
                if (object != null) {
                    empty = false;
                    jsonRow.add(String.valueOf(column), object);
                }
            }

            if (!empty) {
                inventory.add(String.valueOf(row), jsonRow);
            }
        }

        sendText(inventory.toString());
    }

    private JsonObject getItem(GameObject[][] matrix, int row, int column) {
        if (row < matrix.length && column < matrix[0].length) {
            GameObject gameObject = matrix[row][column];
            return gameObject == null ? null : gameObject.getItem();
        } else {
            return null;
        }
    }

}
