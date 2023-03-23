package com.semtleWebGroup.youtubeclone.global.error.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;

/**
 * 로직상 유효하지 않은 값일 경우 사용하는 Exception
 * 쿠폰 만료, 아이디 중복 등
 */
public class InvalidValueException extends BusinessException{
    protected InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }
    protected InvalidValueException(String value, ErrorCode errorCode){
        super(value, errorCode);
    }
}