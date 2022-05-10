package com.wallet.security.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.response.Response;
import com.wallet.security.dto.JwtAuthenticationDTO;
import com.wallet.security.dto.TokenDTO;
import com.wallet.security.utils.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<Response<TokenDTO>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDTO authenticationDto, BindingResult result) throws AuthenticationException {
	Response<TokenDTO> response = new Response<TokenDTO>();

	if (result.hasErrors()) {
	    result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword()));
	SecurityContextHolder.getContext().setAuthentication(authentication);

	UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
	String token = jwtTokenUtil.getToken(userDetails);
	response.setData(new TokenDTO(token));

	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}