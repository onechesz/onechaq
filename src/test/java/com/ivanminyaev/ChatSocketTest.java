package com.ivanminyaev;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.websockets.next.BasicWebSocketConnector;
import io.quarkus.websockets.next.WebSocketClientConnection;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
class ChatSocketTest {

    @Inject
    Instance<BasicWebSocketConnector> connector;

    @TestHTTPResource("/")
    URI uri;

    @Test
    void openTabReceivesMessageFromAnotherConnection() throws Exception {
        String body = "socket-" + UUID.randomUUID();
        BlockingQueue<String> received = new LinkedBlockingQueue<>();
        WebSocketClientConnection listener = connector.get()
                .baseUri(uri)
                .path("/chat")
                .onTextMessage((connection, message) -> received.add(message))
                .connectAndAwait();
        WebSocketClientConnection sender = connector.get()
                .baseUri(uri)
                .path("/chat")
                .connectAndAwait();
        try {
            sender.sendTextAndAwait(body);
            assertEquals(body, received.poll(15, TimeUnit.SECONDS));
            assertEquals(1L, QuarkusTransaction.requiringNew().call(() -> Message.count("body = ?1", body)));
        } finally {
            listener.closeAndAwait();
            sender.closeAndAwait();
            QuarkusTransaction.requiringNew().run(() -> Message.delete("body = ?1", body));
        }
    }

    @Test
    void blankMessageIsNotStoredOrSent() throws Exception {
        BlockingQueue<String> received = new LinkedBlockingQueue<>();
        WebSocketClientConnection listener = connector.get()
                .baseUri(uri)
                .path("/chat")
                .onTextMessage((connection, message) -> received.add(message))
                .connectAndAwait();
        WebSocketClientConnection sender = connector.get()
                .baseUri(uri)
                .path("/chat")
                .connectAndAwait();
        try {
            sender.sendTextAndAwait("   ");
            assertNull(received.poll(1, TimeUnit.SECONDS));
        } finally {
            listener.closeAndAwait();
            sender.closeAndAwait();
        }
    }
}
