package api.dtos;

import java.math.BigDecimal;
import java.time.Instant;

import api.enums.Currency;
import api.enums.Status;

public class AuctionDto {
	
	private int id;
	private int productId;
	private String ownerEmail;
	private BigDecimal startPrice;
	private Currency currency;
	private BigDecimal currentHighestBid;
	private String currentWinnerEmail;
	private Instant createdAt;
	private Instant closedAt;
	private Status status;
	
	public AuctionDto() {
		
	}
	
	public AuctionDto(int id, int productId, String ownerEmail, BigDecimal startPrice, Currency currency,
			BigDecimal currentHighestBid, String currentWinnerEmail, Instant createdAt, Instant closedAt, 
			Status status) {
		super();
		this.id = id;
		this.productId = productId;
		this.ownerEmail = ownerEmail;
		this.startPrice = startPrice;
		this.currency = currency;
		this.currentHighestBid = currentHighestBid;
		this.currentWinnerEmail = currentWinnerEmail;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.status = status;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public String getOwnerEmail() {
		return ownerEmail;
	}
	
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	
	public BigDecimal getStartPrice() {
		return startPrice;
	}
	
	public void setStartPrice(BigDecimal startPrice) {
		this.startPrice = startPrice;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public BigDecimal getCurrentHighestBid() {
		return currentHighestBid;
	}
	
	public void setCurrentHighestBid(BigDecimal currentHighestBid) {
		this.currentHighestBid = currentHighestBid;
	}
	
	public String getCurrentWinnerEmail() {
		return currentWinnerEmail;
	}
	
	public void setCurrentWinnerEmail(String currentWinnerEmail) {
		this.currentWinnerEmail = currentWinnerEmail;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	public Instant getClosedAt() {
		return closedAt;
	}
	
	public void setClosedAt(Instant closedAt) {
		this.closedAt = closedAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
