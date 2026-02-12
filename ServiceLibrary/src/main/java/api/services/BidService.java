package api.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public interface BidService {
	
	@PostMapping("/bids")
	ResponseEntity<?> createBid(@RequestParam int id, @RequestParam BigDecimal amount, 
			@RequestHeader(value = "X-Auth-Email") String currentEmail);

	@GetMapping("/bids/auction/id")
	ResponseEntity<?> getAuctionBids(@RequestParam int id);
	
	@GetMapping("/bids/my-bids")
	ResponseEntity<?> getCurrentUserBids(@RequestHeader(value = "X-Auth-Email") String currentEmail);
	
}
