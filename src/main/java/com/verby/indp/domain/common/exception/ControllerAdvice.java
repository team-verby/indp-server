package com.verby.indp.domain.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        String exName = exception.getClass().getSimpleName();
        String exMessage = exception.getMessage();
        log.error("[Exception] exName=[{}] exMessage=[{}]", exName, exMessage);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse("알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }

}
