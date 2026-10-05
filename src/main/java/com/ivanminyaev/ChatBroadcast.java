package com.ivanminyaev;

import io.quarkus.websockets.next.OpenConnections;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ChatBroadcast {

    @Inject
    OpenConnections connections;

    @Incoming("messages-in")
    @Blocking
    public void onMessage(String body) {
        for (WebSocketConnection open : connections.findByEndpointId("chat")) {
            open.sendTextAndAwait(body);
        }
    }
}
