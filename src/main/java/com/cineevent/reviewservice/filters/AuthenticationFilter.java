package com.cineevent.reviewservice.filters;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cineevent.reviewservice.communicator.UserServiceCommunicator;
import com.cineevent.reviewservice.dto.response.UserAuthResponseDTO;
import com.cineevent.reviewservice.exceptions.InValidAccessTokenException;
import com.cineevent.reviewservice.exceptions.MissingAuthorizationHeaderException;
import com.cineevent.reviewservice.security.ThreadLocalAuthStore;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Component("authenticationFilter")
@Log4j2
public class AuthenticationFilter extends OncePerRequestFilter{
	
	public static final String ACCESS_TOKEN = "accessToken"; 
	public static final String AUTHENTICATION_SCHEME = "Bearer";
	
	@Autowired
	private UserServiceCommunicator userServiceCommunicator;
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String requestMethod = request.getMethod();
		return requestMethod.equalsIgnoreCase("GET");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("In Authentication Filter and Request is:: "+request.getRequestURI());
		authenticate(request);
		filterChain.doFilter(request, response);
	}
	
	private void authenticate(HttpServletRequest request){
		String authorizationAHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.isEmpty(authorizationAHeader)) {
			throw new MissingAuthorizationHeaderException("Authorization header is missing");
		}
		
		String accessToken = authorizationAHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
		
		if(StringUtils.isEmpty(accessToken)) {
			throw new InValidAccessTokenException("Authorization header value is not correct");
		}
		
		UserAuthResponseDTO authResponseDTO = userServiceCommunicator.validateToken(accessToken);
		
		ThreadLocalAuthStore.setAccessToken(accessToken);
		ThreadLocalAuthStore.setAuthDetails(authResponseDTO);
	}

}
