package com.ivanminyaev;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.Instant;

@ApplicationScoped
public class MessageStore {

    @Transactional
    public void save(String body) {
        Message message = new Message();
        message.body = body;
        message.createdAt = Instant.now();
        message.persist();
    }
}
