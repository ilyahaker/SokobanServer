package io.ilyahaker.sokobanserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ilyahaker.sokobanserver.menu.Menu;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.utils.Pair;
import io.ilyahaker.websocket.Websocket;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socoban/")
public class SocobanSocket extends Websocket {
    private GameSession session;

    @Override
    protected void onOpen(Session session) {
        Pair<Integer, Integer> playerPosition = new Pair<>(1, 1);
        this.session = new GameSession(new Menu(), this, session);
    }

    @Override
    protected void onText(Session session, String message) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(message.toString());
        JsonObject object = element.getAsJsonObject();
        if (!object.has("type")) {
            return;
        }

        if (!object.get("type").getAsString().equals("click")) {
            return;
        }

        int row = object.get("i").getAsInt();
        int column = object.get("j").getAsInt();
        this.session.handleClick(row, column);
    }


    public void sendInventory(Session session, GameObject[][] matrix, int currentRow, int currentColumn) {
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

        sendText(inventory.toString(), session);
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
