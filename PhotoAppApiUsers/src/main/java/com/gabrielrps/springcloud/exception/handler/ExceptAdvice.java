package com.gabrielrps.springcloud.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gabrielrps.springcloud.exception.EmailFoundException;
import com.gabrielrps.springcloud.exception.ExceptionResponse;

@ControllerAdvice
public class ExceptAdvice extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(EmailFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleEmailFound(Exception ex, WebRequest req){
		ExceptionResponse exRes = new ExceptionResponse(ex.getMessage(), req.getDescription(false), new Date());
		return new ResponseEntity<ExceptionResponse>(exRes, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleEmailNotFound(Exception ex, WebRequest req){
		ExceptionResponse exRes = new ExceptionResponse(ex.getMessage(), req.getDescription(false), new Date());
		return new ResponseEntity<ExceptionResponse>(exRes, HttpStatus.NOT_FOUND);
	}

}
