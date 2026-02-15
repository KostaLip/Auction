package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.UserDto;

/* LOCAL
@FeignClient(name = "user-service", url = "http://localhost:8770") */
@FeignClient(name = "user-service", url = "http://user-service:8770")
public interface UserProxy {

	@GetMapping("/users/emailAuth")
	ResponseEntity<UserDto> getUserByEmailAuth(@RequestParam String email);
	
	@GetMapping("/internal/users/exists")
    ResponseEntity<Boolean> checkUserExists(@RequestParam String email);
	
}
