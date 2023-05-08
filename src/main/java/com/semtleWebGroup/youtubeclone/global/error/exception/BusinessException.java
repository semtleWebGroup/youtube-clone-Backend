package com.semtleWebGroup.youtubeclone.global.error.exception;


import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;

/**
 * 비즈니스 로직상 발생하는 예외의 부모
 */
public class BusinessException extends RuntimeException{

    private ErrorCode errorCode;

    protected BusinessException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    protected BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}