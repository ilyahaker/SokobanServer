package io.ilyahaker.sokobanserver;

import com.google.gson.JsonObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.GamePlayerImpl;
import io.ilyahaker.utils.Pair;
import io.ilyahaker.websocket.Websocket;
import lombok.Getter;

import java.util.HashMap;

public class GameSession {

    private final GameObject[][] matrix;

    private int currentColumn = 0, currentRow = 0;

    private final HashMap<GameObject, Pair<Integer, Integer>> boxes = new HashMap<>();

//    private Pair<Integer, Integer> playerPosition;

    private final SokobanWebsocket websocket;

    public GameSession(GameObject[][] matrix, SokobanWebsocket websocket) {
//        this.playerPosition = playerPosition;
        this.websocket = websocket;
//        this.currentRow = matrix.length > 6 ? Math.max(playerPosition.getKey() - 3, 0) : 0;
//        this.currentColumn = matrix[0].length > 9 ? Math.max(playerPosition.getValue() - 5, 0) : 0;
        this.matrix = matrix;

        fillInventory();
//        fillPlayers();
    }

    private void fillInventory() {
        websocket.sendInventory(matrix, currentRow, currentColumn);
    }

    private void fillPlayers() {
//        inventory.setItem((playerPosition.getKey() - currentRow) * 9 + playerPosition.getValue() - currentColumn, new GamePlayerImpl().getItem());
    }

//    public void handleClick(int row, int column) {
//        int differenceRow = playerPosition.getKey() - row - currentRow,
//                differenceColumn = playerPosition.getValue() - column - currentColumn;
//
//        //inverse XOR operation
//        if ((Math.abs(differenceRow) == 1 && Math.abs(differenceColumn) == 0) == (Math.abs(differenceColumn) == 1 && Math.abs(differenceRow) == 0)) {
//            return;
//        }
//
//        Pair<Integer, Integer> currentPlayerPosition = new Pair<>(row + currentRow, column + currentColumn);
//        boolean needFilling = true;
//
//        //changing the starting point depending on player's position
//        if (differenceRow < 0 && matrix.length - currentPlayerPosition.getKey() >= 3 && currentPlayerPosition.getKey() >= 3) {
//            currentRow = Math.max(currentPlayerPosition.getKey() - 3, 0);
//        } else if (differenceRow > 0 && matrix.length - currentPlayerPosition.getKey() >= 4 && currentPlayerPosition.getKey() >= 2) {
//            currentRow = Math.max(currentPlayerPosition.getKey() - 2, 0);
//        } else if (differenceColumn < 0 && matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
//            currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
//        } else if (differenceColumn > 0 && matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
//            currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
//        } else {
//            needFilling = false;
//        }
//
//        if (needFilling) {
//            fillInventory();
//        } else {
//            setItem(playerPosition.getKey(), playerPosition.getValue());
//        }
//
//        playerPosition = currentPlayerPosition;
//        fillPlayers();
//    }
}
