package com.dajanapirjasi.UrlShortener.repository;

import com.dajanapirjasi.UrlShortener.model.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl,Long> {

    Optional<ShortenedUrl> findByShortUrl(String shortUrl);

    boolean existsByShortUrl(String code);

    Optional<ShortenedUrl> findByLongUrlAndExpiresAtAfter(String longUrl, Instant expirationTime);

    List<ShortenedUrl> findByExpiresAtBeforeAndIsDeletedFalse(Instant expirationTime);
}
