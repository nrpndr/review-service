package com.cineevent.reviewservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.cineevent.reviewservice.filters.AuthenticationFilter;
import com.cineevent.reviewservice.filters.AuthorizationFilter;
import com.cineevent.reviewservice.filters.ErrorHandlerFilter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components().addSecuritySchemes("bearer-key",
				new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.formLogin().disable();
		http.csrf().disable();
		http.cors().disable();
		http.authorizeHttpRequests((authz) -> authz.anyRequest().permitAll());
		return http.build();
	}

	@Bean
    public FilterRegistrationBean<ErrorHandlerFilter> errorFilterRegistrationBean(@Autowired ErrorHandlerFilter errorHandlerFilter){
        FilterRegistrationBean<ErrorHandlerFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setName("errorHandlerFilter");
        registrationBean.setFilter(errorHandlerFilter);
        registrationBean.addUrlPatterns("/movies","/movies/*","/events","/events/*");
        registrationBean.setOrder(1);
        registrationBean.setEnabled(true);
        return registrationBean;
    }
	
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistrationBean(
			@Autowired AuthenticationFilter authenticationFilter) {
		FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setName("authenticationFilter");
		registrationBean.setFilter(authenticationFilter);
		registrationBean.addUrlPatterns("/movies","/movies/*","/events","/events/*");
		registrationBean.setEnabled(true);
		registrationBean.setOrder(2);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegistrationBean(
			@Autowired AuthorizationFilter authorizationFilter) {
		FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setName("authorizationFilter");
		registrationBean.setFilter(authorizationFilter);
		registrationBean.addUrlPatterns("/movies","/movies/*","/events","/events/*");
		registrationBean.setOrder(3);
		registrationBean.setEnabled(true);
		return registrationBean;
	}

}
