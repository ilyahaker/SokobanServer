package io.ilyahaker.sokobanserver;

import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.websocket.Websocket;
import io.ilyahaker.websocket.WebsocketServer;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;

import javax.servlet.ServletContext;
import javax.websocket.DeploymentException;
import java.net.Socket;

public class SokobanServer extends WebsocketServer {

    public SokobanServer(int port) {
        super(port);
    }

    @Override
    public void initWebsocket(ServletContext servletContext, ServerContainer wsContainer) throws DeploymentException {
        wsContainer.setDefaultMaxTextMessageBufferSize(65535);
        wsContainer.addEndpoint(SocobanSocket.class);
    }
}
