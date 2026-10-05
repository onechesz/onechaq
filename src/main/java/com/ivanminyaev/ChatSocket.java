package com.ivanminyaev;

import io.quarkus.websockets.next.*;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@WebSocket(path = "/chat", endpointId = "chat")
public class ChatSocket {

    @Inject
    MessageStore store;

    @Inject
    OnlinePresence presence;

    @Inject
    @Channel("messages-out")
    Emitter<String> emitter;

    @OnOpen
    @Blocking
    public void onOpen(WebSocketConnection connection) {
        presence.join(connection.id());
    }

    @OnClose
    @Blocking
    public void onClose(WebSocketConnection connection) {
        presence.leave(connection.id());
    }

    @OnTextMessage
    public void onMessage(String raw) {
        String body = raw == null ? "" : raw.trim();
        if (body.isEmpty() || body.length() > 255) {
            return;
        }
        store.save(body);
        emitter.send(body);
    }
}
