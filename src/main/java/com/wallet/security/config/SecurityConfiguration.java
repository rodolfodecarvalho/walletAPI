package com.wallet.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wallet.security.JwtAuthenticationEntryPoint;
import com.wallet.security.JwtAuthenticationTokenFilter;
import com.wallet.security.utils.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
	return super.authenticationManagerBean();
    }

    @Bean(name = "passwordEncoder")
    public static PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

    @Bean(name = "authenticationTokenFilter")
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
	return new JwtAuthenticationTokenFilter(userDetailsService, jwtTokenUtil);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers("/auth/**", "/configuration/security", "/webjars/**", "/user/**", "/v2/api-docs", "/v3/api-docs", "/swagger-resources/**", "/swagger-ui/**")
		.permitAll().anyRequest().authenticated();

	http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	http.headers().cacheControl();
    }
}