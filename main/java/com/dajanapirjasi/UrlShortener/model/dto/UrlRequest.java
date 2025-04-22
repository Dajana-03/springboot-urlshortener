package com.dajanapirjasi.UrlShortener.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UrlRequest {
    @NotBlank
    String longUrl;
    Long expirationSeconds;
}
