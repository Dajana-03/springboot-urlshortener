package com.dajanapirjasi.UrlShortener.exception;

public class GeneralException extends RuntimeException {

    public static final String URL_NOT_FOUND = "Short URL not found.";
    public static final String URL_EXPIRED = "The short URL has expired.";
    public static final String REGISTRATION_FAILED = "Registration failed.";
    public static final String AUTHENTICATION_FAILED = "Invalid username or password.";
    public static final String SUCCESS = "Operation completed successfully.";

    public GeneralException(String message) {
        super(message);
    }
}
