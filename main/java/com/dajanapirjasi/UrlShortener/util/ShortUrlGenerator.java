package com.dajanapirjasi.UrlShortener.util;


import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Component
public class ShortUrlGenerator {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DEFAULT_LENGTH = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Set<String> generatedCache = new HashSet<>();


    public String generate() {
        return generate(DEFAULT_LENGTH);
    }


    public String generate(int length) {
        StringBuilder shortCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = SECURE_RANDOM.nextInt(BASE62.length());
            shortCode.append(BASE62.charAt(index));
        }

        return shortCode.toString();
    }


    public String generateUnique(int length, java.util.function.Predicate<String> existsCheck) {
        int attempts = 0;
        int maxRetries = 10;

        while (attempts < maxRetries) {
            String candidate = generate(length);
            if (!existsCheck.test(candidate)) {
                return candidate;
            }
            attempts++;
        }

        throw new IllegalStateException("Unable to generate a unique short URL after " + maxRetries + " attempts.");
    }
}
