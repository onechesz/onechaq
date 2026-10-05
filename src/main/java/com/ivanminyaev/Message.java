package com.ivanminyaev;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.Instant;

@Entity
public class Message extends PanacheEntity {
    public String body;
    public Instant createdAt;
}
