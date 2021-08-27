package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.database.api.Database;
import io.ilyahaker.sokobanserver.levels.FinalDisplay;
import io.ilyahaker.sokobanserver.levels.Level;
import io.ilyahaker.sokobanserver.levels.PassedLevel;
import io.ilyahaker.sokobanserver.menu.Menu;
import io.ilyahaker.sokobanserver.objects.*;
import io.ilyahaker.utils.Pair;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.Session;
import java.util.List;

public class GameSession {

    @Getter
    @Setter
    private GameObject[][] map, currentMenu;

    @Setter
    private int currentColumn = 0, currentRow = 0;

    private final GamePlayer player;

    private final SocobanSocket websocket;

    private final Session session;

    @Getter
    private final Menu menu;

    @Setter
    private Level currentLevel;

    private final Database database;

    private int steps = 0;

    @Setter
    private long countFinish;
    private long filledFinishes;

    @Getter
    @Setter
    private State state;

    public GameSession(Menu menu, SocobanSocket websocket, Session session, GamePlayer player) {
        this.menu = menu;
        this.websocket = websocket;
        this.session = session;
        this.currentMenu = menu.getMenu();
        this.database = Main.getDatabase();
        this.player = player;
        this.state = State.MENU;
    }

    public void fillInventory() {
        switch (state) {
            case MENU, FINISHED_GAME -> websocket.sendInventory(session, currentMenu, currentRow, currentColumn);
            case GAME -> websocket.sendInventory(session, map, currentRow, currentColumn);
            default -> throw new IllegalStateException("You must choose session state!");
        }
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

        if (finalRow >= map.length || finalColumn >= map[0].length || finalColumn < 0 || finalRow < 0) {
            return false;
        }

        MovableGameObjectImpl movableGameObject = (MovableGameObjectImpl) gameObject;
        GameObject object = map[finalRow][finalColumn];
        switch (direction) {
            case RIGHT:
                if (moveObject(finalRow, finalColumn + 1, object, direction)) {
                    if (movableGameObject.getType() == GameObjectType.BOX
                            && movableGameObject.getUnderObject() != null && movableGameObject.getUnderObject().getType() == GameObjectType.FINISH) {
                        filledFinishes--;
                    }

                    map[finalRow][finalColumn - 1] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(map[finalRow][finalColumn]);
                    map[finalRow][finalColumn] = movableGameObject;

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

                    map[finalRow][finalColumn + 1] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(map[finalRow][finalColumn]);
                    map[finalRow][finalColumn] = movableGameObject;

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

                    map[finalRow + 1][finalColumn] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(map[finalRow][finalColumn]);
                    map[finalRow][finalColumn] = movableGameObject;

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

                    map[finalRow - 1][finalColumn] = movableGameObject.getUnderObject();
                    movableGameObject.setUnderObject(map[finalRow][finalColumn]);
                    map[finalRow][finalColumn] = movableGameObject;

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
        switch (state) {
            case MENU -> {
                if (currentMenu[row][row] != null && currentMenu[row][column].getType() == GameObjectType.BUTTON) {
                    ((ButtonObject) currentMenu[row][column]).click(this);
                }
            }
            case GAME -> {
                int finalRow = row + currentRow,
                        finalColumn = column + currentColumn;

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

                    if (map.length - currentPlayerPosition.getKey() >= 3 && currentPlayerPosition.getKey() >= 3) {
                        currentRow = Math.max(currentPlayerPosition.getKey() - 3, 0);
                    }
                } else if (differenceRow > 0) {
                    if (!moveObject(finalRow, finalColumn, player, Direction.DOWN)) {
                        return;
                    }

                    if (map.length - currentPlayerPosition.getKey() >= 4 && currentPlayerPosition.getKey() >= 2) {
                        currentRow = Math.max(currentPlayerPosition.getKey() - 2, 0);
                    }
                } else if (differenceColumn < 0) {
                    if (!moveObject(finalRow, finalColumn, player, Direction.RIGHT)) {
                        return;
                    }

                    if (map[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
                        currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
                    }
                } else if (differenceColumn > 0) {
                    if (!moveObject(finalRow, finalColumn, player, Direction.LEFT)) {
                        return;
                    }

                    if (map[0].length - currentPlayerPosition.getValue() >= 5 && currentPlayerPosition.getValue() >= 4) {
                        currentColumn = Math.max(currentPlayerPosition.getValue() - 4, 0);
                    }
                }

                player.setCoordinateX(currentPlayerPosition.getKey());
                player.setCoordinateY(currentPlayerPosition.getValue());
                steps++;
                fillInventory();

                if (filledFinishes == countFinish) {
                    System.out.println("PLAYER WON!!!!");

                    PassedLevel passedLevel = player.getPassedLevel(currentLevel.getId());
                    if (passedLevel == null) {
                        Main.getDatabase().async()
                                .prepareUpdate("""
                                insert into sokoban_passed_levels VALUES (?, ?, ?, ?);
                                """)
                                .execute(player.getId(), currentLevel.getId(), steps, steps);

                        player.putPassedLevel(currentLevel.getId(), new PassedLevel(currentLevel, steps, steps));
                    } else {
                        Main.getDatabase().async()
                                .prepareUpdate("""
                                update sokoban_passed_levels set (steps, last_steps) = (?, ?)
                                where player_id = ? and level_id = ?;
                                """)
                                .execute(Math.min(passedLevel.getSteps(), steps), steps,
                                        player.getId(), currentLevel.getId());

                        passedLevel.setLastSteps(steps);
                        passedLevel.setSteps(Math.min(steps, passedLevel.getSteps()));
                    }

                    this.currentMenu = new FinalDisplay(List.of("You have finished with " + steps + " steps!")).getDisplay();
                    state = State.FINISHED_GAME;
                    currentLevel = null;
                    filledFinishes = 0;
                    currentRow = 0;
                    currentColumn = 0;
                    steps = 0;

                    fillInventory();
                }

            }
            case FINISHED_GAME -> {
                state = State.MENU;
                this.currentMenu = menu.getMenu();
                fillInventory();
            }
        }
    }

    public enum Direction {
        RIGHT, LEFT, DOWN, UP
    }

}
