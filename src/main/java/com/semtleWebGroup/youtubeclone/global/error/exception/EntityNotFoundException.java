package com.semtleWebGroup.youtubeclone.global.error.exception;

import com.semtleWebGroup.youtubeclone.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{
    public EntityNotFoundException(String message){
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
