package auctionService.serviceImplementation;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import api.dtos.ProductDto;
import api.proxies.ProductProxy;
import auctionService.entity.AuctionModel;
import auctionService.entity.AuctionParticipantModel;
import auctionService.entity.BidModel;

@Service
public class AuctionLoggerService {

	private static final String LOG_DIR = "/var/log/auction/auctions/";
	
	@Autowired
	private ProductProxy productProxy;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public void logAuctionCompleted(
			AuctionModel auction, List<AuctionParticipantModel> participants, List<BidModel> bids) {
		try {
            ProductDto product = productProxy.getProductsById(auction.getProductId()).getBody();
            
            AuctionCompletedLog log = new AuctionCompletedLog();
            log.eventType = "AUCTION_COMPLETED";
            log.timestamp = Instant.now().toString();
            log.auctionId = auction.getId();
            log.status = auction.getStatus().name();
            
            log.productId = product.getId();
            log.productName = product.getName();
            log.productDescription = product.getDescription();
            log.sellerEmail = auction.getOwnerEmail();
            
            log.winnerEmail = auction.getCurrentWinnerEmail();
            log.winningBid = auction.getCurrentHighestBid();
            log.startPrice = auction.getStartPrice();
            log.currency = auction.getCurrency().name();
            
            log.totalBids = bids.size();
            log.totalParticipants = participants.size();
            log.auctionDurationMinutes = java.time.Duration.between(
                auction.getCreatedAt(), auction.getClosedAt()
            ).toMinutes();
            log.createdAt = auction.getCreatedAt().toString();
            log.closedAt = auction.getClosedAt().toString();
            
            log.participants = participants.stream()
                    .map(p -> new ParticipantInfo(
                        p.getParticipantEmail(),
                        p.getDeposit(),
                        p.getJoinedAt().toString()
                    ))
                    .toList();
            
            log.bids = bids.stream()
                    .map(b -> new BidInfo(
                        b.getUserEmail(),
                        b.getAmount(),
                        b.getCreatedAt().toString()
                    ))
                    .toList();
            
            String filename = String.format("auction-completed-%d-%d.json", 
                    auction.getId(), 
                    System.currentTimeMillis()
                );
                
            writeToFile(filename, log);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logAuctionCancelled(
			AuctionModel auction, List<AuctionParticipantModel> participants, String reason) {
		try {
            ProductDto product = productProxy.getProductsById(auction.getProductId()).getBody();
            
            AuctionCancelledLog log = new AuctionCancelledLog();
            log.eventType = "AUCTION_CANCELLED";
            log.timestamp = Instant.now().toString();
            log.auctionId = auction.getId();
            log.status = auction.getStatus().name();
            
            log.productId = product.getId();
            log.productName = product.getName();
            log.sellerEmail = auction.getOwnerEmail();
            
            log.reason = reason;
            log.cancelledBy = auction.getOwnerEmail();
            log.startPrice = auction.getStartPrice();
            log.currency = auction.getCurrency().name();
            log.createdAt = auction.getCreatedAt().toString();
            log.closedAt = auction.getClosedAt().toString();
            
            log.totalParticipants = participants.size();
            log.participants = participants.stream()
                .map(p -> new ParticipantInfo(
                    p.getParticipantEmail(),
                    p.getDeposit(),
                    p.getJoinedAt().toString()
                ))
                .toList();
            
            String filename = String.format("auction-cancelled-%d-%d.json", 
                    auction.getId(), 
                    System.currentTimeMillis()
                );
            
            writeToFile(filename, log);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeToFile(String filename, Object log) {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(LOG_DIR));
            
            String json = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(log);
            
            try (FileWriter writer = new FileWriter(LOG_DIR + filename)) {
                writer.write(json);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	static class AuctionCompletedLog {
        public String eventType;
        public String timestamp;
        public int auctionId;
        public String status;
        public int productId;
        public String productName;
        public String productDescription;
        public String sellerEmail;
        public String winnerEmail;
        public BigDecimal winningBid;
        public BigDecimal startPrice;
        public String currency;
        public int totalBids;
        public int totalParticipants;
        public long auctionDurationMinutes;
        public String createdAt;
        public String closedAt;
        public List<ParticipantInfo> participants;
        public List<BidInfo> bids;
    }
	
	static class AuctionCancelledLog {
        public String eventType;
        public String timestamp;
        public int auctionId;
        public String status;
        public int productId;
        public String productName;
        public String sellerEmail;
        public String reason;
        public String cancelledBy;
        public BigDecimal startPrice;
        public String currency;
        public String createdAt;
        public String closedAt;
        public int totalParticipants;
        public List<ParticipantInfo> participants;
    }
	
	static class ParticipantInfo {
        public String email;
        public BigDecimal deposit;
        public String joinedAt;
        
        public ParticipantInfo(String email, BigDecimal deposit, String joinedAt) {
            this.email = email;
            this.deposit = deposit;
            this.joinedAt = joinedAt;
        }
    }
	
	static class BidInfo {
        public String bidderEmail;
        public BigDecimal amount;
        public String placedAt;
        
        public BidInfo(String bidderEmail, BigDecimal amount, String placedAt) {
            this.bidderEmail = bidderEmail;
            this.amount = amount;
            this.placedAt = placedAt;
        }
    }
	
}
