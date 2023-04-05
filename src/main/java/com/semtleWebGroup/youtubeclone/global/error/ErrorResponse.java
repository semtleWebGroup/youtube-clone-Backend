package com.semtleWebGroup.youtubeclone.global.error;

import com.semtleWebGroup.youtubeclone.global.dto.ResponseDto;
import com.semtleWebGroup.youtubeclone.global.error.exception.EntityNotFoundException;
import com.semtleWebGroup.youtubeclone.global.error.exception.InvalidValueException;
import com.semtleWebGroup.youtubeclone.global.error.exception.LocalResourceException;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;


/**
 * Error Response
 * use {@link ErrorResponse#of(ErrorCode, BindingResult)} method to create
 */
@Getter
public class ErrorResponse extends ResponseDto {
    private String message;
    private String status;
    private List<FieldError> errors;
    private String code;

    private ErrorResponse(ErrorCode code, List<FieldError> errors){
        super(false);
        this.message = code.getMessage();
        this.status = code.getStatus().toString();
        this.errors = errors;
        this.code = code.getCode();
    }

    private ErrorResponse(ErrorCode code){
        super(false);
        this.message = code.getMessage();
        this.status = code.getStatus().toString();
        this.errors = new ArrayList<>(); //if errors is null, response empty list instead of null
        this.code = code.getCode();
    }

    public static ErrorResponse of(ErrorCode code){
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, BindingResult bindingResult){
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e){
        String value = e.getValue() == null ? "" : e.getValue().toString();
        List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE,errors);
    }

    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> fieldErrors){
        return new ErrorResponse(errorCode,fieldErrors);
    }


    public static ErrorResponse of(EntityNotFoundException e) {
        return new ErrorResponse(ErrorCode.ENTITY_NOT_FOUND);
    }

    public static ErrorResponse of(LocalResourceException e) {
        return new ErrorResponse(e.getErrorCode());
    }
}