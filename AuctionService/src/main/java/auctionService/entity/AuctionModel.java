package auctionService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import api.enums.Currency;
import api.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;

@Entity
public class AuctionModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "Auction_SEQ_GENERATOR", sequenceName = "auction_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Auction_SEQ_GENERATOR")
	private int id;
	
	@Column(nullable = false)
	private int productId;
	
	@Column(nullable = false)
	private String ownerEmail;
	
	@Column(nullable = false)
	private BigDecimal startPrice;
	
	@Column(nullable = false)
	private Currency currency;
	
	private BigDecimal currentHighestBid;
	private String currentWinnerEmail;
	
	private Instant createdAt;
	private Instant closedAt;
	
	private Status status;
	
	@PrePersist
    void onCreate() {
        createdAt = Instant.now();
        status = Status.ACTIVE;
        closedAt = null;
    }
	
	public AuctionModel() {
		
	}

	public AuctionModel(int productId, String ownerEmail, BigDecimal startPrice, Currency currency,
			BigDecimal currentHighestBid, String currentWinnerEmail) {
		super();
		this.productId = productId;
		this.ownerEmail = ownerEmail;
		this.startPrice = startPrice;
		this.currency = currency;
		this.currentHighestBid = currentHighestBid;
		this.currentWinnerEmail = currentWinnerEmail;
	}

	public AuctionModel(int id, int productId, String ownerEmail, BigDecimal startPrice, Currency currency,
			BigDecimal currentHighestBid, String currentWinnerEmail, Instant createdAt, Instant closedAt) {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
