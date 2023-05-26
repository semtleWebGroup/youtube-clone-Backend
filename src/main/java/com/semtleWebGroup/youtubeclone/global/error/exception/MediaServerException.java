package com.semtleWebGroup.youtubeclone.global.error.exception;

import org.springframework.web.client.HttpClientErrorException;

public class MediaServerException extends RuntimeException {
    private final HttpClientErrorException httpClientErrorException;

    public MediaServerException(HttpClientErrorException httpClientErrorException) {
        this.httpClientErrorException = httpClientErrorException;
    }
}
