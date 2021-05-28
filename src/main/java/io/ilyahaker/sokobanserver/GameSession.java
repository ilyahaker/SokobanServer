package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.GamePlayer;
import io.ilyahaker.sokobanserver.objects.GamePlayerImpl;
import io.ilyahaker.utils.Pair;

import java.util.HashMap;

public class GameSession {

    private final GameObject[][] matrix;

    private int currentColumn = 0, currentRow = 0;

    private GamePlayer player;

    private final SokobanWebsocket websocket;

    public GameSession(GameObject[][] matrix, Pair<Integer, Integer> playerPosition, SokobanWebsocket websocket) {
        this.websocket = websocket;
        this.currentRow = matrix.length > 6 ? Math.max(playerPosition.getKey() - 3, 0) : 0;
        this.currentColumn = matrix[0].length > 9 ? Math.max(playerPosition.getValue() - 5, 0) : 0;
        this.matrix = matrix;

        this.player = new GamePlayerImpl(playerPosition.getKey(), playerPosition.getValue());
        player.setUnderObject(matrix[player.getCoordinateX()][player.getCoordinateY()]);
        matrix[player.getCoordinateX()][player.getCoordinateY()] = player;
        fillInventory();
    }

    private void fillInventory() {
        websocket.sendInventory(matrix, currentRow, currentColumn);
    }

    public void handleClick(int row, int column) {
        int differenceRow = player.getCoordinateX() - row - currentRow,
                differenceColumn = player.getCoordinateY() - column - currentColumn;

        //inverse XOR operation
        if ((Math.abs(differenceRow) == 1 && Math.abs(differenceColumn) == 0) == (Math.abs(differenceColumn) == 1 && Math.abs(differenceRow) == 0)) {
            return;
        }

        if (row >= matrix.length || column >= matrix[0].length) {
            return;
        }

        Pair<Integer, Integer> currentPlayerPosition = new Pair<>(row + currentRow, column + currentColumn);

        //changing the starting point depending on player position
        if (differenceRow < 0 && matrix.length - currentPlayerPosition.getKey() >= 3 && currentPlayerPosition.getKey() >= 3) {
            currentRow = Math.max(currentPlayerPosition.getKey() - 3, 0);
        } else if (differenceRow > 0 && matrix.length - currentPlayerPosition.getKey() >= 4 && currentPlayerPosition.getKey() >= 2) {
            currentRow = Math.max(currentPlayerPosition.getKey() - 2, 0);
        } else if (differenceColumn < 0 && matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
            currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
        } else if (differenceColumn > 0 && matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
            currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
        }


        matrix[player.getCoordinateX()][player.getCoordinateY()] = player.getUnderObject();
        player.setUnderObject(matrix[currentPlayerPosition.getKey()][currentPlayerPosition.getValue()]);
        matrix[currentPlayerPosition.getKey()][currentPlayerPosition.getValue()] = player;
        player.setCoordinateX(currentPlayerPosition.getKey());
        player.setCoordinateY(currentPlayerPosition.getValue());
        fillInventory();
    }
}
