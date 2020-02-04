package com.gabrielrps.springcloud.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler{

	@ExceptionHandler(TokenExpired.class)
	public ResponseEntity<?> handleExpiredToken(String message, WebRequest req){
		ExceptionResponse ex = new ExceptionResponse(message, req.getDescription(false), new Date());
		return new ResponseEntity<>(ex, HttpStatus.FORBIDDEN);
	}

}
