package com.semtleWebGroup.youtubeclone.global.error;


import com.semtleWebGroup.youtubeclone.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.nio.file.AccessDeniedException;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @Validated 로 binding error 발생시 발생
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할 경우 발생
     * 주로 @RequestBody 나 @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("handleMethodArgumentNotValidException", e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @ModelAttribut 으로 binding error 발생시 발생
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e){
        log.error("handleBindException", e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum 으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        log.error("handleMethodArgumentTypeMismatchException",e);
        ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원되지 않은 HTTP method 호출
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.error("handleHttpRequestMethodNotSupportedException",e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response,HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e){
        log.error("handleAccessDeniedException",e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED);
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }

    /**
     * 위에서 처리 되지 않은 비즈니스 로직상 발생하는 Exception
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e){
        log.error("handleBusinessException",e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response,errorCode.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity handle(MaxUploadSizeExceededException e){
        return ResponseEntity.internalServerError().build();
    }

    /**
     * 분류되지 않은 에러
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("handleEntityNotFoundException",e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}