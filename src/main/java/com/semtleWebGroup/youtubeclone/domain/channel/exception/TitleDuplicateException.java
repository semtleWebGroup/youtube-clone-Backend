package com.semtleWebGroup.youtubeclone.domain.channel.exception;

import com.semtleWebGroup.youtubeclone.global.error.exception.InvalidValueException;

public class TitleDuplicateException extends InvalidValueException {
    public TitleDuplicateException(String value) {
        super(value);
    }
}
