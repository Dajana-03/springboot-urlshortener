package com.dajanapirjasi.UrlShortener.service.scheduler;

import com.dajanapirjasi.UrlShortener.model.ShortenedUrl;
import com.dajanapirjasi.UrlShortener.repository.ShortenedUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpiredUrlCleaner {

    private  final ShortenedUrlRepository shortenedUrlRepository;

    @Scheduled(fixedRate = 60000)
    public void cleanUp() {
        Instant now = Instant.now();
        List<ShortenedUrl> expiredUrls = shortenedUrlRepository.findByExpiresAtBeforeAndIsDeletedFalse(now);
        for (ShortenedUrl url : expiredUrls) {
            url.setDeleted(true);
            shortenedUrlRepository.save(url);
        }
    }



}


