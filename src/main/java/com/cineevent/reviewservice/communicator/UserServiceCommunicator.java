package com.cineevent.reviewservice.communicator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cineevent.reviewservice.dto.request.TokenDTO;
import com.cineevent.reviewservice.dto.response.UserAuthResponseDTO;
import com.cineevent.reviewservice.exceptions.AccessTokenExpiredException;
import com.cineevent.reviewservice.exceptions.ServiceCommunicationException;
import com.google.gson.Gson;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UserServiceCommunicator {

	@Value("${userservice.url}")
	private String userServiceURL;

	@Value("${userservice.validateToken.path}")
	private String validateTokenPath;

	@Autowired
	private RestTemplate restTemplate;

	private Gson gson = new Gson();

	public UserAuthResponseDTO validateToken(String accessToken) {

		String validateTokenEndpoint = userServiceURL + validateTokenPath;
		TokenDTO tokenDTO = new TokenDTO(accessToken);
		try {
			log.info("Calling userService to validate the token, tokenEndpoint url {}", validateTokenEndpoint);
			ResponseEntity<String> response = restTemplate.postForEntity(validateTokenEndpoint, tokenDTO, String.class);

			HttpStatusCode code = response.getStatusCode();
			String responseBody = response.getBody();

			log.info("ResponseBody={}, responseCode={}", responseBody, code.value());
			if (code.is2xxSuccessful()) {
				UserAuthResponseDTO userAuthResponseDTO1 = gson.fromJson(responseBody, UserAuthResponseDTO.class);
				return userAuthResponseDTO1;
			}
			throw new ServiceCommunicationException("Internal Server Error");
		} catch (RestClientException e) {
			log.error("Error communicating to user service, errorMessage", e);
			if(e.getMessage().contains("401")) {
				throw new AccessTokenExpiredException("Access Token is Expired or InValid");
			}
			throw new ServiceCommunicationException("Auth Service is unavailable, please try after some time");
		} catch (Exception e) {
			log.error("Error communicating to user service, errorMessage={}", e);
			throw new ServiceCommunicationException("Internal Server Error");
		}
	}
}
