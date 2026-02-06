package auth_service.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.UserDto;
import api.proxies.UserProxy;
import util.exceptions.UnauthorizedException;

@RestController
public class AuthController {
	
	@Autowired
    private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserProxy userProxy;

	@PostMapping("/validate")
	public ResponseEntity<?> validate(@RequestHeader("Authorization") String auth
			/*@RequestHeader("X-Original-URI") String originalUri, 
			@RequestHeader("X-Original-Method") String originalMethod*/) {
		String base64 = auth.replace("Basic ", "");
        String decoded = new String(Base64.getDecoder().decode(base64));
        String[] parts = decoded.split(":", 2);
        
        if (parts.length < 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String email = parts[0];
        String password = parts[1];
        
        UserDto user;
        try {
        		user = userProxy.getUserByEmailAuth(email).getBody();
        } catch(Exception e) {
        		throw new UnauthorizedException("WRONG EMAIL OR PASSWORD");
        }
        
        if (user == null || !encoder.matches(password, user.getPassword())) {
        		throw new UnauthorizedException("WRONG EMAIL OR PASSWORD");
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Email", user.getEmail());
        headers.add("X-Auth-Role", user.getRole().name());
        
        return ResponseEntity.ok().headers(headers).build();
        
	}
	
}
