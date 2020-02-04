package com.gabrielrps.springcloud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.gabrielrps.springcloud.data.UserEntity;
import com.gabrielrps.springcloud.exception.EmailFoundException;
import com.gabrielrps.springcloud.model.shared.UserDto;
import com.gabrielrps.springcloud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		if(userRepository.findByEmail(userDetails.getEmail()).isPresent()) throw new EmailFoundException("E-mail já cadastrado no sistema");

		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

		UserEntity userCreated = userRepository.save(userEntity);
		
		UserDto dtoRetorno = modelMapper.map(userCreated, UserDto.class);

		return dtoRetorno;
	}

	public ResponseEntity<?> errorMap(BindingResult result){

		Map<String, String> errorM = new HashMap<>();

		for (FieldError error : result.getFieldErrors()) {
			errorM.put(error.getField(), error.getDefaultMessage());
		}

		return new ResponseEntity<>(errorM, HttpStatus.BAD_REQUEST);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userEntity = userRepository.findByEmail(username);
		if(!userEntity.isPresent()) {
			throw new UsernameNotFoundException("E-mail não cadastrado no sistema");
		}
		
		return new User(userEntity.get().getEmail(), userEntity.get().getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		Optional<UserEntity> userEntity = userRepository.findByEmail(email);
		if(!userEntity.isPresent()) {
			throw new UsernameNotFoundException("E-mail não cadastrado no sistema");
		}
		
		return new ModelMapper().map(userEntity.get(), UserDto.class);
	}

}
