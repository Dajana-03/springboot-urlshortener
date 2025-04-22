package com.dajanapirjasi.UrlShortener.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;


@Entity
@Table(name = "ShortenedUrl")
@Data
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String shortUrl;
    @Column(nullable = false, length = 2048)
    private String longUrl;
    private Instant createdAt;
    private Instant expiresAt;
    private long clickCount;
    private boolean isDeleted = false;


    public void refreshExpiration(Instant newExpiration) {
        this.expiresAt = newExpiration;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    public void countClicks() {
        this.clickCount++;
    }

}
