package com.dajanapirjasi.UrlShortener.service.impl;


import com.dajanapirjasi.UrlShortener.exception.GeneralException;
import com.dajanapirjasi.UrlShortener.model.ShortenedUrl;
import com.dajanapirjasi.UrlShortener.repository.ShortenedUrlRepository;
import com.dajanapirjasi.UrlShortener.service.ShortenedUrlService;
import com.dajanapirjasi.UrlShortener.util.ShortUrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortenedUrlServiceImpl implements ShortenedUrlService {

    private final ShortenedUrlRepository shortenedUrlRepository;
    private final ShortUrlGenerator shortUrlGenerator;

    @Value("${url.expiration.default:300}")
    private long defaultExpirationSeconds;


    @Override
    @Transactional
    public String createShortUrl(String longUrl, Long customExpirationSeconds) {
        Instant now = Instant.now();
        long expirationSeconds = (customExpirationSeconds != null) ? customExpirationSeconds : defaultExpirationSeconds;
        Instant expirationTime = now.plusSeconds(expirationSeconds);

        Optional<ShortenedUrl> existingUrl = shortenedUrlRepository.findByLongUrlAndExpiresAtAfter(longUrl,Instant.now());

        if (existingUrl.isPresent()) {
            // URL exists and is not expired, return the already shortened URL
            ShortenedUrl url = existingUrl.get();
            url.refreshExpiration(expirationTime);
            shortenedUrlRepository.save(url);
            return url.getShortUrl();
        } else {
            String shortUrl = shortUrlGenerator.generateUnique(8, shortenedUrlRepository::existsByShortUrl);
            ShortenedUrl newUrl = new ShortenedUrl();
            newUrl.setShortUrl(shortUrl);
            newUrl.setLongUrl(longUrl);
            newUrl.setCreatedAt(now);
            newUrl.setExpiresAt(expirationTime);
            newUrl.setClickCount(0L);
            newUrl.setDeleted(false);
            shortenedUrlRepository.save(newUrl);
            return shortUrl;
        }
    }

    @Override
    @Transactional
    public String getOriginalUrl(String shortCode) {
        ShortenedUrl url = shortenedUrlRepository.findByShortUrl(shortCode)
                .orElseThrow(() -> new GeneralException(GeneralException.URL_NOT_FOUND));

        if (url.isExpired()) {
            shortenedUrlRepository.delete(url);
            throw new GeneralException(GeneralException.URL_EXPIRED);
        }
        url.countClicks();
        shortenedUrlRepository.save(url);
        return url.getLongUrl();
    }



}
