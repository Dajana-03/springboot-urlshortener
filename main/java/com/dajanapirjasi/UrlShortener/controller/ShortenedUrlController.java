package com.dajanapirjasi.UrlShortener.controller;
import com.dajanapirjasi.UrlShortener.exception.GeneralException;
import com.dajanapirjasi.UrlShortener.model.dto.UrlRequest;
import com.dajanapirjasi.UrlShortener.model.dto.UrlResponse;
import com.dajanapirjasi.UrlShortener.service.ShortenedUrlService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
public class ShortenedUrlController {

    private final ShortenedUrlService shortenedUrlService;

    @ApiOperation(value = "create short url from long url")
    @PostMapping("/shorten")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UrlResponse> shortenUrl(
            @Valid @RequestBody UrlRequest request)
    {
        String shortUrl= shortenedUrlService.createShortUrl(
                request.getLongUrl(),
                request.getExpirationSeconds()
        );
        return ResponseEntity.ok(new UrlResponse(shortUrl));
    }

    @ApiOperation(value = "Redirect", notes = "taking generated short Url, return corresponding long url")
    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getLongUrl(@PathVariable String shortUrl) {
        try {
            String longUrl = shortenedUrlService.getOriginalUrl(shortUrl);
            return ResponseEntity.ok(longUrl);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(404).body(GeneralException.URL_NOT_FOUND);
        }
    }

}
