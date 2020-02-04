package com.gabrielrps.springcloud.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;

import com.gabrielrps.springcloud.model.shared.UserDto;

public interface UserService extends UserDetailsService{
	
	public UserDto createUser(UserDto user);

	public ResponseEntity<?> errorMap(BindingResult result);
	
	public UserDto getUserDetailsByEmail(String email);

}
