package com.semtleWebGroup.youtubeclone.global.error.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;
import com.semtleWebGroup.youtubeclone.global.error.FieldError;

import java.util.Collections;
import java.util.List;

/**
 * 로직상 유효하지 않은 값일 경우 사용하는 Exception
 * 쿠폰 만료, 아이디 중복 등
 */
public class InvalidValueException extends BusinessException{
    private List<FieldError> fieldErrors;
    public InvalidValueException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
        this.fieldErrors = Collections.emptyList();
    }
    public InvalidValueException(String message, ErrorCode errorCode, List<FieldError> fieldErrors){
        super(message, errorCode);
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}