package com.jooany.letsdeal.exception;

import com.jooany.letsdeal.controller.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LetsDealAppException.class)
    public ResponseEntity<?> applicationHandler(LetsDealAppException e) {
        log.error("Error occurs {}", e.toString());

        if(e.getMessage() != null){
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(Response.error(e.getErrorCode().name(), e.getMessage()));
        }

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> applicationHandler(RuntimeException e) {
//        log.error("Error occurs {}", e.toString());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
//    }

}
