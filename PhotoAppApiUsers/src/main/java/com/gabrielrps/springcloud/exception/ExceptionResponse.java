package com.gabrielrps.springcloud.exception;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable{

	private static final long serialVersionUID = -9214590772267630710L;
	
	private String message;
	private String details;
	private Date date;
	
	public ExceptionResponse(String message, String details, Date date) {
		this.message = message;
		this.details = details;
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return details;
	}
	public Date getDate() {
		return date;
	}
}
