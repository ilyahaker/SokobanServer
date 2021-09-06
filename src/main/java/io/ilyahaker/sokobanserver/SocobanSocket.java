package io.ilyahaker.sokobanserver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ilyahaker.sokobanserver.database.api.Database;
import io.ilyahaker.sokobanserver.database.api.result.SelectResult;
import io.ilyahaker.sokobanserver.menu.Menu;
import io.ilyahaker.sokobanserver.menu.SideMenu;
import io.ilyahaker.sokobanserver.objects.ButtonObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.GamePlayer;
import io.ilyahaker.sokobanserver.objects.GamePlayerImpl;
import io.ilyahaker.websocket.Websocket;
import lombok.SneakyThrows;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@ServerEndpoint(value = "/socoban/")
public class SocobanSocket extends Websocket {
    private GameSession session;

    @Override
    protected void onOpen(Session session) {
    }

    @SneakyThrows
    @Override
    protected void onText(Session session, String message) {
        Database database = Main.getDatabase();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(message.toString());
        JsonObject object = element.getAsJsonObject();
        if (object.has("init")) {
            String[] messages = object.get("init").getAsString().split(" ");
            JsonObject result = switch (messages[0]) {
                case "login" -> {
                    JsonObject open = new JsonObject();
                    if (messages.length != 3) {
                        open.addProperty("open", false);
                        open.addProperty("error",
                                "You don't have enough arguments. Enter /socket login [login] [password].");
                        yield open;
                    }

                    String login = messages[1];
                    String password = messages[2];
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                    String encodedPassword = Base64.getEncoder().encodeToString(hash);

                    SelectResult selectResult = database.sync().prepareSelect("""
                            select * from sokoban_users where login = ?;
                            """).execute(login);
                    if (selectResult.getRows().size() == 0) {
                        database.async().prepareUpdate("""
                            insert into sokoban_users (login, password)
                            VALUES (?, ?);
                            """).execute(login, encodedPassword);
                    } else {
                        String currentPassword = selectResult.getRows().get(0).getString("password");
                        if (!encodedPassword.equals(currentPassword)) {
                            open.addProperty("open", false);
                            open.addProperty("error",
                                    "Incorrect password!");
                            yield open;
                        }
                    }

                    int id = selectResult.getRows().get(0).getInt("id");
                    open.addProperty("open", true);
                    GamePlayer player = new GamePlayerImpl(id, login);
                    this.session = new GameSession(new Menu(player), this, session, player, new SideMenu(player));
                    yield open;
                }
                default -> {
                    JsonObject open = new JsonObject();
                    open.addProperty("open", false);
                    open.addProperty("error",
                            "You should log in!");
                    yield open;
                }
            };

            JsonObject init = new JsonObject();
            init.add("init", result);
            sendText(init.toString(), session);

            if (result.get("open").getAsBoolean()) {
                this.session.fillInventory();
            } else {
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Player has not logged in!"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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


    public void sendInventory(Session session, GameObject[][] matrix, int currentRow, int currentColumn, SideMenu sideMenu) {
        JsonObject inventory = new JsonObject();
        ButtonObject[] objects = sideMenu.getButtons();
        for (int row = 0; row < 6; row++) {
            boolean empty = true;
            JsonObject jsonRow = new JsonObject();
            for (int column = 0; column < 8; column++) {
                JsonObject object = getItem(matrix, row + currentRow, column + currentColumn);
                if (object != null) {
                    empty = false;
                    jsonRow.add(String.valueOf(column), object);
                }
            }

            JsonObject object = getItem(objects[row]);
            if (object != null) {
                empty = false;
                jsonRow.add(Integer.toString(8), object);
            }

            if (!empty) {
                inventory.add(String.valueOf(row), jsonRow);
            }
        }

        sendText(inventory.toString(), session);
    }

    private JsonObject getItem(GameObject[][] matrix, int row, int column) {
        if (row < matrix.length && column < matrix[0].length) {
            return getItem(matrix[row][column]);
        } else {
            return null;
        }
    }

    private JsonObject getItem(GameObject gameObject) {
        return gameObject == null ? null : gameObject.getItem();
    }
}
