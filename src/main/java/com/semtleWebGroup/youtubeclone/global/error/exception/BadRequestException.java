package com.semtleWebGroup.youtubeclone.global.error.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;

import java.util.Collections;
import java.util.List;

/**
 * 클라이언트의 잘못된 요청
 */
public class BadRequestException extends RuntimeException {

    private ErrorCode errorCode;
    private List<FieldError> fieldErrors;

    public BadRequestException(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
        this.errorCode = ErrorCode.INVALID_INPUT_VALUE;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
