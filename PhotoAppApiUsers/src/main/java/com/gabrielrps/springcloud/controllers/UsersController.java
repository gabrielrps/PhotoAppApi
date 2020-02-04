package com.gabrielrps.springcloud.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielrps.springcloud.model.shared.UserDto;
import com.gabrielrps.springcloud.model.ui.CreateUserRequestModel;
import com.gabrielrps.springcloud.model.ui.CreateUserResponseModel;
import com.gabrielrps.springcloud.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/status/check")
	public String status() {
		return "Working... " + environment.getProperty("local.server.port") + " token = "+ environment.getProperty("token.secret");
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
				 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestModel user, BindingResult result) {
		if(result.hasErrors()) return userService.errorMap(result);
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = modelMapper.map(user, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		
		CreateUserResponseModel returUser = modelMapper.map(createdUser, CreateUserResponseModel.class);
		
		return new ResponseEntity<CreateUserResponseModel>(returUser, HttpStatus.CREATED);
	}
	

}
