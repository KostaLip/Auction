package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import api.enums.Status;

@FeignClient(name = "auction-service", url = "http://localhost:8400")
public interface AuctionProxy {

	@GetMapping("/auctions/status")
	ResponseEntity<?> getAuctionsByStatus(@RequestParam Status status);
	
}
