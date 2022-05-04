package com.wallet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.wallet.security.utils.JwtTokenUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

@Configuration
@Profile("dev")
public class SwaggerConfig {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public Docket api() {
	return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.wallet.controller")).paths(PathSelectors.any()).build();
    }

    @Bean
    public SecurityConfiguration security() {
	String token;
	try {
	    UserDetails userDetails = this.userDetailsService.loadUserByUsername("development@swagger.user");
	    token = this.jwtTokenUtil.getToken(userDetails);
	} catch (Exception e) {
	    token = "";
	}

	return SecurityConfigurationBuilder.builder().clientId(ApiKeyVehicle.HEADER.getValue()).clientSecret("Bearer " + token).appName("Authorization").scopeSeparator(",").build();
    }

    private ApiInfo apiInfo() {
	return new ApiInfoBuilder().title("Wallet API").description("Wallet API - Documentação de acesso aos endpoints.").version("1.0").build();
    }
}