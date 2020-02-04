package com.gabrielrps.springcloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class TokenExpired extends RuntimeException{
	
	private static final long serialVersionUID = -8935347725984779978L;

	private String message;

	public TokenExpired(String message) {
		super(message);
	}
}
