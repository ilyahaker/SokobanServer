package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.database.api.Database;
import io.ilyahaker.sokobanserver.levels.Level;
import io.ilyahaker.sokobanserver.menu.Menu;
import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;

import javax.websocket.Session;
import java.util.Arrays;

public class GameSession {

    private GameObject[][] matrix;

    private int currentColumn = 0, currentRow = 0;

    private GamePlayer player;

    private final SocobanSocket websocket;

    private final Session session;

    private final Menu menu;

    private final Database database;

    private long countFinish;
    private long filledFinishes;

    public GameSession(Menu menu, SocobanSocket websocket, Session session) {
        this.menu = menu;
        this.websocket = websocket;
        this.session = session;
        this.matrix = menu.getMenu();
        this.database = Main.getDatabase();
    }

    public void fillInventory() {
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
        int finalRow = row + currentRow,
                finalColumn = column + currentColumn;

        if (matrix[finalRow][finalColumn] != null) {
            switch (matrix[finalRow][finalColumn].getType()) {
                case CHOOSE_LEVEL:
                    Level level = menu.chooseLevel(finalRow, finalColumn);
                    matrix = Arrays.stream(level.getMap())
                            .map(gameObjects ->
                                Arrays.stream(gameObjects)
                                        .map(object -> object != null ? object.copy() : null)
                                        .toArray(GameObject[]::new)
                            )
                            .toArray(GameObject[][]::new);
                    currentRow = level.getStartCurrentRow();
                    currentColumn = level.getStartCurrentColumn();

                    filledFinishes = 0;
                    countFinish = Arrays.stream(matrix)
                            .flatMap(Arrays::stream)
                            .filter(object -> object != null && object.getType() == GameObjectType.FINISH)
                            .count();

                    this.player = new GamePlayerImpl(level.getPlayerPosition().getKey(), level.getPlayerPosition().getValue());
                    player.setUnderObject(matrix[player.getCoordinateX()][player.getCoordinateY()]);
                    matrix[player.getCoordinateX()][player.getCoordinateY()] = player;

                    fillInventory();
                    return;
                case PAGE_UP:
                    menu.pageUp();
                    this.matrix = menu.getMenu();

                    fillInventory();
                    return;
                case PAGE_DOWN:
                    menu.pageDown();
                    this.matrix = menu.getMenu();

                    fillInventory();
                    return;
            }
        }

        int differenceRow = player.getCoordinateX() - finalRow,
                differenceColumn = player.getCoordinateY() - finalColumn;

        //inverse XOR operation
        if ((Math.abs(differenceRow) == 1 && Math.abs(differenceColumn) == 0) == (Math.abs(differenceColumn) == 1 && Math.abs(differenceRow) == 0)) {
            return;
        }

        Pair<Integer, Integer> currentPlayerPosition = new Pair<>(finalRow, finalColumn);

        //changing the starting point depending on player position
        if (differenceRow < 0) {
            if (!moveObject(finalRow, finalColumn, player, Direction.UP)) {
                return;
            }

            if (matrix.length - currentPlayerPosition.getKey() >= 3 && currentPlayerPosition.getKey() >= 3) {
                currentRow = Math.max(currentPlayerPosition.getKey() - 3, 0);
            }
        } else if (differenceRow > 0) {
            if (!moveObject(finalRow, finalColumn, player, Direction.DOWN)) {
                return;
            }

            if (matrix.length - currentPlayerPosition.getKey() >= 4 && currentPlayerPosition.getKey() >= 2) {
                currentRow = Math.max(currentPlayerPosition.getKey() - 2, 0);
            }
        } else if (differenceColumn < 0) {
            if (!moveObject(finalRow, finalColumn, player, Direction.RIGHT)) {
                return;
            }

            if (matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
                currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
            }
        } else if (differenceColumn > 0) {
            if (!moveObject(finalRow, finalColumn, player, Direction.LEFT)) {
                return;
            }

            if (matrix[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
                currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
            }
        }

        player.setCoordinateX(currentPlayerPosition.getKey());
        player.setCoordinateY(currentPlayerPosition.getValue());
        fillInventory();

        if (filledFinishes == countFinish) {
            System.out.println("PLAYER WON!!!!");
            this.matrix = menu.getMenu();
            currentRow = 0;
            currentColumn = 0;

            fillInventory();
        }
    }

    public enum Direction {
        RIGHT, LEFT, DOWN, UP
    }

}
