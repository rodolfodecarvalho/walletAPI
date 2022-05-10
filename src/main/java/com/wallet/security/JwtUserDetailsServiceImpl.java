package com.wallet.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wallet.entity.User;
import com.wallet.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	Optional<User> user = userService.findByEmail(email);

	if (user.isPresent()) {
	    return JwtUserFactory.create(user.get());
	}

	throw new UsernameNotFoundException("Email não encontrado.");
    }
}