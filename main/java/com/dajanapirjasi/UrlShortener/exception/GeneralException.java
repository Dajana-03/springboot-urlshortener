package com.dajanapirjasi.UrlShortener.exception;

public class GeneralException extends RuntimeException {

    public static final String URL_NOT_FOUND = "Short URL not found or has expired.";
    public static final String URL_EXPIRED = "The short URL has expired.";



    public GeneralException(String message) {
        super(message);
    }
}
