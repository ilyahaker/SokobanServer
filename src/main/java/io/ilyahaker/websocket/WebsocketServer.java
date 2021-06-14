package io.ilyahaker.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.servlet.ServletContext;
import javax.websocket.DeploymentException;

public class WebsocketServer {

    private final int port;

    public WebsocketServer(int port) {
        this.port = port;
    }

    public void initWebsocket(ServletContext servletContext, ServerContainer wsContainer) throws DeploymentException {
        // Configure defaults for container
        wsContainer.setDefaultMaxTextMessageBufferSize(65535);

        // Add WebSocket endpoint to javax.websocket layer
        wsContainer.addEndpoint(Websocket.class);
    }

    public void loadServer() {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        try {
            // Initialize javax.websocket layer
            WebSocketServerContainerInitializer.configure(context, (servletContext, wsContainer) -> {
                // This lambda will be called at the appropriate place in the
                // ServletContext initialization phase where you can initialize
                // and configure  your websocket container.
                initWebsocket(servletContext, wsContainer);
            });

            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
