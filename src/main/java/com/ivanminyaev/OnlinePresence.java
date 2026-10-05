package com.ivanminyaev;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.websockets.next.OpenConnections;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OnlinePresence {

    static final String PREFIX = "online:";
    static final long TTL_SECONDS = 60;

    @Inject
    RedisDataSource redis;

    @Inject
    OpenConnections connections;

    public void join(String connectionId) {
        redis.value(String.class).set(PREFIX + connectionId, "1", new SetArgs().ex(TTL_SECONDS));
    }

    public void leave(String connectionId) {
        redis.key().del(PREFIX + connectionId);
    }

    public int count() {
        return redis.key().keys(PREFIX + "*").size();
    }

    @Scheduled(every = "20s")
    void refresh() {
        for (WebSocketConnection connection : connections.findByEndpointId("chat")) {
            join(connection.id());
        }
    }
}
