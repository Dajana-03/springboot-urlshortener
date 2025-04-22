package com.dajanapirjasi.UrlShortener.service;


import com.dajanapirjasi.UrlShortener.exception.GeneralException;
import com.dajanapirjasi.UrlShortener.model.ShortenedUrl;
import com.dajanapirjasi.UrlShortener.repository.ShortenedUrlRepository;
import com.dajanapirjasi.UrlShortener.service.impl.ShortenedUrlServiceImpl;
import com.dajanapirjasi.UrlShortener.util.ShortUrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ShortenedUrlServiceImplTest {

    private ShortenedUrlRepository shortenedUrlRepository;
    private ShortUrlGenerator shortUrlGenerator;
    private ShortenedUrlServiceImpl shortenedUrlService;

    @BeforeEach
    void setUp() {
        shortenedUrlRepository = mock(ShortenedUrlRepository.class);
        shortUrlGenerator = mock(ShortUrlGenerator.class);
        shortenedUrlService = new ShortenedUrlServiceImpl(shortenedUrlRepository, shortUrlGenerator);
    }


    @Test
    void createShortUrl_whenUrlAlreadyExists_shouldReturnExistingShortUrlAndRefresh() {
        String longUrl = "http://example.com";
        ShortenedUrl existing = new ShortenedUrl();
        existing.setShortUrl("abc123");
        existing.setLongUrl(longUrl);
        existing.setExpiresAt(Instant.now().plusSeconds(300));
        existing.setClickCount(0L);
        existing.setDeleted(false);

        when(shortenedUrlRepository.findByLongUrlAndExpiresAtAfter(eq(longUrl), any()))
                .thenReturn(Optional.of(existing));

        String shortUrl = shortenedUrlService.createShortUrl(longUrl, null);

        assertEquals("abc123", shortUrl);
        verify(shortenedUrlRepository).save(existing);
    }

    @Test
    void getOriginalUrl_whenValidShortUrl_shouldReturnLongUrlAndIncrementClicks() {
        String shortCode = "abc123";
        ShortenedUrl url = new ShortenedUrl();
        url.setShortUrl(shortCode);
        url.setLongUrl("http://example.com");
        url.setExpiresAt(Instant.now().plusSeconds(300));
        url.setClickCount(0L);
        url.setDeleted(false);

        when(shortenedUrlRepository.findByShortUrl(shortCode)).thenReturn(Optional.of(url));

        String longUrl = shortenedUrlService.getOriginalUrl(shortCode);

        assertEquals("http://example.com", longUrl);
        assertEquals(1L, url.getClickCount());
        verify(shortenedUrlRepository).save(url);
    }

    @Test
    void getOriginalUrl_whenUrlExpired_shouldThrowAndDelete() {
        String shortCode = "expired123";
        ShortenedUrl url = new ShortenedUrl();
        url.setShortUrl(shortCode);
        url.setLongUrl("http://expired.com");
        url.setExpiresAt(Instant.now().minusSeconds(1));

        when(shortenedUrlRepository.findByShortUrl(shortCode)).thenReturn(Optional.of(url));

        GeneralException ex = assertThrows(GeneralException.class, () ->
                shortenedUrlService.getOriginalUrl(shortCode)
        );
        assertEquals(GeneralException.URL_EXPIRED, ex.getMessage());
        verify(shortenedUrlRepository).delete(url);
    }


}
