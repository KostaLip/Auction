package api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.AuctionCreateUpdateDto;

@Service
public interface AuctionService {

	@PostMapping("/auctions")
	ResponseEntity<?> createAuction(@RequestBody AuctionCreateUpdateDto dto, 
			@RequestHeader(value = "X-Auth-Email") String currentEmail);
	
	@GetMapping("/auctions")
	ResponseEntity<?> getAuctions();
	
	@GetMapping("/auctions/id")
	ResponseEntity<?> getAuctionById(@RequestParam int id);
	
	@GetMapping("/auctions/email")
	ResponseEntity<?> getAuctionByEmail(@RequestParam String email);
	
}
