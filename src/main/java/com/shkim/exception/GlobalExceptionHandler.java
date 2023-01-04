package com.shkim.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * GlobalExceptionHandler
 * : 예외 처리(코드 및 문구 지정)
 *
 * @author shkim
 */
@SuppressWarnings("rawtypes")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity handleInvalidRequestException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        printMessage(request, e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("InvalidRequestException 잘못된 요청입니다. "+request.getAttribute("tid"));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity handleSqlException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        printMessage(request, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SQLException 데이터를 처리할 수 없습니다. "+request.getAttribute("tid"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        printMessage(request, e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("RuntimeException 서비스를 이용할 수 없습니다. "+request.getAttribute("tid"));
    }

    private void printMessage(HttpServletRequest request, Exception e){
        e.printStackTrace();
        log.error("InvalidRequestException:     "+e.getMessage());
        log.error("[TID]    "+request.getAttribute("tid"));
        log.error("[REQUEST]   "+request.getMethod()+request.getAttribute("requestBody"));
        log.error("[ERROR]  "+e.toString());
    }
}