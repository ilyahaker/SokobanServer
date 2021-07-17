package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Arrays;

public class GameSession {

    private final GameObject[][] matrix;

    private int currentColumn = 0, currentRow = 0;

    private GamePlayer player;

    private final SocobanSocket websocket;

    private final Session session;

    private final long countFinish;
    private long filledFinishes;

    public GameSession(GameObject[][] matrix, Pair<Integer, Integer> playerPosition, SocobanSocket websocket, Session session) {
        this.websocket = websocket;
        this.session = session;
        this.currentRow = matrix.length > 6 ? Math.max(playerPosition.getKey() - 3, 0) : 0;
        this.currentColumn = matrix[0].length > 9 ? Math.max(playerPosition.getValue() - 5, 0) : 0;
        this.matrix = matrix;

        countFinish = Arrays.stream(matrix)
                .flatMap(Arrays::stream)
                .filter(object -> object != null && object.getType() == GameObjectType.FINISH)
                .count();

        this.player = new GamePlayerImpl(playerPosition.getKey(), playerPosition.getValue());
        player.setUnderObject(matrix[player.getCoordinateX()][player.getCoordinateY()]);
        matrix[player.getCoordinateX()][player.getCoordinateY()] = player;
        fillInventory();
    }

    private void fillInventory() {
        websocket.sendInventory(session, matrix, currentRow, currentColumn);
    }

    private boolean moveObject(int finalRow, int finalColumn, GameObject gameObject, Direction direction) {
        if (gameObject == null) {
            return true;
        }

        if (gameObject.getType() == GameObjectType.WALL) {
            return false;
        }

        if (gameObject.getType() != GameObjectType.MOVABLE && gameObject.getType() != GameObjectType.BOX) {
            return true;
        }

        if (finalRow >= matrix.length || finalColumn >= matrix[0].length || finalColumn < 0 || finalRow < 0) {
            return false;
        }

        MovableGameObjectImpl movableGameObject = (MovableGameObjectImpl) gameObject;
        GameObject object = matrix[finalRow][finalColumn];
        switch (direction) {
            case RIGHT:
                if (moveObject(finalRow, finalColumn + 1, object, direction)) {
                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes--;
                    }

                    matrix[finalRow][finalColumn - 1] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(matrix[finalRow][finalColumn]);
                    matrix[finalRow][finalColumn] = movableGameObject;

                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes++;
                    }

                    return true;
                }
                break;
            case LEFT:
                if (moveObject(finalRow, finalColumn - 1, object, direction)) {
                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes--;
                    }

                    matrix[finalRow][finalColumn + 1] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(matrix[finalRow][finalColumn]);
                    matrix[finalRow][finalColumn] = movableGameObject;

                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes++;
                    }

                    return true;
                }
                break;
            case DOWN:
                if (moveObject(finalRow - 1, finalColumn, object, direction)) {
                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes--;
                    }

                    matrix[finalRow + 1][finalColumn] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(matrix[finalRow][finalColumn]);
                    matrix[finalRow][finalColumn] = movableGameObject;

                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes++;
                    }

                    return true;
                }
                break;
            case UP:
                if (moveObject(finalRow + 1, finalColumn, object, direction)) {
                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes--;
                    }

                    matrix[finalRow - 1][finalColumn] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(matrix[finalRow][finalColumn]);
                    matrix[finalRow][finalColumn] = movableGameObject;

                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes++;
                    }

                    return true;
                }
                break;
        }

        return false;
    }

    public void handleClick(int row, int column) {
        int differenceRow = player.getCoordinateX() - row - currentRow,
                differenceColumn = player.getCoordinateY() - column - currentColumn;

        //inverse XOR operation
        if ((Math.abs(differenceRow) == 1 && Math.abs(differenceColumn) == 0) == (Math.abs(differenceColumn) == 1 && Math.abs(differenceRow) == 0)) {
            return;
        }

        Pair<Integer, Integer> currentPlayerPosition = new Pair<>(row + currentRow, column + currentColumn);

        //changing the starting point depending on player position
        if (differenceRow < 0) {
            if (!moveObject(row + currentRow, column + currentColumn, player, Direction.UP)) {
                return;
            }

            if (matrix.length - currentPlayerPosition.getKey() >= 3 && currentPlayerPosition.getKey() >= 3) {
                currentRow = Math.max(currentPlayerPosition.getKey() - 3, 0);
            }
        } else if (differenceRow > 0) {
            if (!moveObject(row + currentRow, column + currentColumn, player, Direction.DOWN)) {
                return;
            }

            if (matrix.length - currentPlayerPosition.getKey() >= 4 && currentPlayerPosition.getKey() >= 2) {
                currentRow = Math.max(currentPlayerPosition.getKey() - 2, 0);
            }
        } else if (differenceColumn < 0) {
            if (!moveObject(row + currentRow, column + currentColumn, player, Direction.RIGHT)) {
                return;
            }

            if (matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
                currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
            }
        } else if (differenceColumn > 0) {
            if (!moveObject(row + currentRow, column + currentColumn, player, Direction.LEFT)) {
                return;
            }

            if (matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
                currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
            }
        }


//        matrix[player.getCoordinateX()][player.getCoordinateY()] = player.getUnderObject();
//        player.setUnderObject(matrix[currentPlayerPosition.getKey()][currentPlayerPosition.getValue()]);
//        matrix[currentPlayerPosition.getKey()][currentPlayerPosition.getValue()] = player;
        player.setCoordinateX(currentPlayerPosition.getKey());
        player.setCoordinateY(currentPlayerPosition.getValue());
        fillInventory();

        if (filledFinishes == countFinish) {
            System.out.println("PLAYER WON!!!!");
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Player has been won!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum Direction {
        RIGHT, LEFT, DOWN, UP
    }

}
