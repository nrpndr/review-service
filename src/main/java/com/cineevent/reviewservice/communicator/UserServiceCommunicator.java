package com.cineevent.reviewservice.communicator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cineevent.reviewservice.dto.response.UserAuthResponseDTO;

@Component
public class UserServiceCommunicator {
    		
    @Value("${userservice.url}")		
	private String userServiceURL;
    
    @Value("${userservice.validateToken.path}")
	private String validateTokenPath;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public UserAuthResponseDTO validateToken(String accessToken) {
    	UserAuthResponseDTO userAuthResponseDTO1 = new UserAuthResponseDTO();
    	userAuthResponseDTO1.setUserId(1);
    	userAuthResponseDTO1.setUserName("nrpndr");
    	userAuthResponseDTO1.setUserRole("ADMIN");
    	
    	
    	UserAuthResponseDTO userAuthResponseDTO2 = new UserAuthResponseDTO();
    	userAuthResponseDTO2.setUserId(2);
    	userAuthResponseDTO2.setUserName("nrpndr");
    	userAuthResponseDTO2.setUserRole("USER");
    	if(accessToken.equals("user1")) {
    		return userAuthResponseDTO1;
    	}else {  
    		return userAuthResponseDTO2;
    	}
    }
}
