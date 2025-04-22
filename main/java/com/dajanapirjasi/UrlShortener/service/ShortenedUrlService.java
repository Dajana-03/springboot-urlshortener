package com.dajanapirjasi.UrlShortener.service;

public interface ShortenedUrlService {

    String createShortUrl(String longUrl, Long customExpirationSeconds);

    String getOriginalUrl(String shortCode);
}
