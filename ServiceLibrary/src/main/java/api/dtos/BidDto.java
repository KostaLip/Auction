package api.dtos;

import java.math.BigDecimal;
import java.time.Instant;

import api.enums.Currency;

public class BidDto {

	private int auctionId;
	private String userEmail;
	private BigDecimal amount;
	private Currency currency;
	private Instant createdAt;
	
	public BidDto() {
		
	}
	
	public BidDto(int auctionId, String userEmail, BigDecimal amount, Currency currency, Instant createdAt) {
		super();
		this.auctionId = auctionId;
		this.userEmail = userEmail;
		this.amount = amount;
		this.currency = currency;
		this.createdAt = createdAt;
	}

	public int getAuctionId() {
		return auctionId;
	}
	
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
}
