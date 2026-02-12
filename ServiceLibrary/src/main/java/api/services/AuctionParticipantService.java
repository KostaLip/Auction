package api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.AuctionParticipantDto;

@Service
public interface AuctionParticipantService {

	@GetMapping("/auctions/participant/email")
	ResponseEntity<?> getAuctionsParticipantByEmail(@RequestParam String email);
	
	@GetMapping("/auctions/participant")
	List<AuctionParticipantDto> getAuctionsParticipants();
	
	@GetMapping("/auctions/participant/id")
	ResponseEntity<?> getAuctionsParticipantsByAuctionId(@RequestParam int id);
	
}
