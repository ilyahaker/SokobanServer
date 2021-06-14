package io.ilyahaker.websocket;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public abstract class Websocket {
    private final CountDownLatch closureLatch = new CountDownLatch(1);

    @OnOpen
    public void onWebSocketConnect(Session sess) {
        System.out.println("Socket Connected: " + sess);
        onOpen(sess);
    }

    @OnMessage
    public void onWebSocketText(Session sess, String message) throws IOException
    {
        onText(sess, message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
        closureLatch.countDown();
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException
    {
        System.out.println("Awaiting closure from remote");
        closureLatch.await();
    }

    protected abstract void onText(Session session, String message);

    protected abstract void onOpen(Session session);

    protected void sendText(String message, Session session) {
        session.getAsyncRemote().sendText(message);
    }
}
