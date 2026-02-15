package auth_service.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.UserDto;
import api.enums.Role;
import api.proxies.UserProxy;
import jakarta.annotation.PostConstruct;
import util.exceptions.PermissionForbiddenException;
import util.exceptions.UnauthorizedException;

@RestController
public class AuthController {
	
	@Autowired
    private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserProxy userProxy;
	
	private 	Map<String, Set<Role>> permissions = new HashMap<>();
	
	@PostConstruct
	private void initPermissions() {
		permissions.put("GET:/users", Set.of(Role.ADMIN));
		permissions.put("GET:/users/email", Set.of(Role.USER, Role.ADMIN));
		permissions.put("POST:/users/newAdmin", Set.of(Role.ADMIN));
		permissions.put("POST:/users/newUser", Set.of(Role.ADMIN));
		permissions.put("PUT:/users", Set.of(Role.USER, Role.ADMIN));
		permissions.put("DELETE:/users", Set.of(Role.USER, Role.ADMIN));

		permissions.put("GET:/bankAccounts", Set.of(Role.ADMIN));
		permissions.put("GET:/bankAccounts/email", Set.of(Role.USER, Role.ADMIN));
		permissions.put("PUT:/bankAccounts", Set.of(Role.ADMIN));
		
		permissions.put("GET:/products", Set.of(Role.USER, Role.ADMIN));
		permissions.put("GET:/products/id", Set.of(Role.USER, Role.ADMIN));
		permissions.put("GET:/products/email", Set.of(Role.USER, Role.ADMIN));
		permissions.put("POST:/products", Set.of(Role.USER));
		permissions.put("PUT:/products/id", Set.of(Role.USER));
		permissions.put("DELETE:/products/id", Set.of(Role.USER));
		
		permissions.put("GET:/auctions", Set.of(Role.USER, Role.ADMIN));
		permissions.put("GET:/auctions/status", Set.of(Role.USER, Role.ADMIN));
		permissions.put("GET:/auctions/id", Set.of(Role.USER, Role.ADMIN));
		permissions.put("GET:/auctions/email", Set.of(Role.USER, Role.ADMIN));
		permissions.put("POST:/auctions", Set.of(Role.USER));
		permissions.put("POST:/auctions/join/id", Set.of(Role.USER));
		permissions.put("POST:/auctions/cancel/id", Set.of(Role.USER));
		permissions.put("POST:/auctions/finish/id", Set.of(Role.USER));
		
		permissions.put("GET:/auctions/participant", Set.of(Role.USER, Role.ADMIN));
		permissions.put("GET:/auctions/participant/email", Set.of(Role.USER, Role.ADMIN));
		permissions.put("GET:/auctions/participant/id", Set.of(Role.USER, Role.ADMIN));
		
		permissions.put("POST:/bids", Set.of(Role.USER));
		permissions.put("GET:/bids/my-bids", Set.of(Role.USER));
		permissions.put("GET:/bids/auction/id", Set.of(Role.USER, Role.ADMIN));
		
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validate(@RequestHeader("Authorization") String auth,
			@RequestHeader("X-Original-URI") String originalUri, 
			@RequestHeader("X-Original-Method") String originalMethod) {
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
        
        Role role = user.getRole();
        
        if(!hasPermissions(role, originalMethod, originalUri)) {
        		throw new PermissionForbiddenException("You do not have permission to access this resource");
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Email", user.getEmail());
        headers.add("X-Auth-Role", user.getRole().name());
        
        return ResponseEntity.ok().headers(headers).build();
        
	}
	
	private boolean hasPermissions(Role role, String method, String uri) {
		String key = method + ":" + extractBasePath(uri);
		Set<Role> allowedRoles = permissions.get(key);
		
		if (allowedRoles == null) {
			return false;
		}
		
		return allowedRoles.contains(role);
	}
	
	private String extractBasePath(String uri) {
		if (uri.contains("?")) {
			uri = uri.substring(0, uri.indexOf("?"));
		}
		return uri;
	}
	
}
