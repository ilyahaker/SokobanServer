package io.ilyahaker.sokobanserver.objects;

import io.ilyahaker.sokobanserver.GameSession;

import java.util.List;
import java.util.function.Consumer;

public class ButtonObjectImpl extends GameObjectImpl implements ButtonObject {

    private final Consumer<GameSession> clickHandler;

    public ButtonObjectImpl(Material material, String name, List<String> lore, Consumer<GameSession> clickHandler) {
        super(material, GameObjectType.BUTTON, name, lore);
        this.clickHandler = clickHandler;
    }

    public ButtonObjectImpl(Material material, String name, List<String> lore) {
        super(material, GameObjectType.BUTTON, name, lore);
        this.clickHandler = session -> {};
    }

    @Override
    public void click(GameSession session) {
        clickHandler.accept(session);
    }

    @Override
    public GameObject copy() {
        return new ButtonObjectImpl(getMaterial(), getName(), getLore(), clickHandler);
    }
}
