package com.wallet.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.response.Response;
import com.wallet.service.UserService;
import com.wallet.util.Bcrypt;
import com.wallet.util.enums.RoleEnum;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "user")
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<Response<UserDTO>> create(@Valid @RequestBody UserDTO dto, BindingResult result) {

	Response<UserDTO> response = new Response<UserDTO>();

	if (result.hasErrors()) {
	    result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	User user = service.save(convertDtoToEntity(dto));

	response.setData(convertEntityToDto(user));

	return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private User convertDtoToEntity(UserDTO dto) {
	User user = new User();

	user.setId(dto.getId());
	user.setName(dto.getName());
	user.setEmail(dto.getEmail());
	user.setPassword(Bcrypt.getHash(dto.getPassword()));
	user.setRole(RoleEnum.valueOf(dto.getRole()));

	return user;
    }

    private UserDTO convertEntityToDto(User user) {
	UserDTO dto = new UserDTO();

	dto.setId(user.getId());
	dto.setName(user.getName());
	dto.setEmail(user.getEmail());
	dto.setRole(user.getRole().toString());

	return dto;
    }
}