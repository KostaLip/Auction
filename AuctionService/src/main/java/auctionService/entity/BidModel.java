package auctionService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import api.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;

@Entity
public class BidModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "Bid_SEQ_GENERATOR", sequenceName = "bid_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Bid_SEQ_GENERATOR")
	private int id;
	
	@Column(nullable = false)
	private int auctionId;
	
	@Column(nullable = false)
	private String userEmail;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Column(nullable = false)
	private Currency currency;
	
	private Instant createdAt;
	
	@PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
	
	public BidModel() {
		
	}

	public BidModel(int auctionId, String userEmail, BigDecimal amount, Currency currency) {
		super();
		this.auctionId = auctionId;
		this.userEmail = userEmail;
		this.amount = amount;
		this.currency = currency;
	}

	public BidModel(int id, int auctionId, String userEmail, BigDecimal amount, Currency currency, Instant createdAt) {
		super();
		this.id = id;
		this.auctionId = auctionId;
		this.userEmail = userEmail;
		this.amount = amount;
		this.currency = currency;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
