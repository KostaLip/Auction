package auctionService.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;

@Entity
public class AuctionParticipantModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "Auction_Participant_SEQ_GENERATOR", sequenceName = "auction_participant_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Auction_Participant_SEQ_GENERATOR")
	private int id;
	
	@Column(nullable = false)
	private int auctionId;
	
	@Column(nullable = false)
	private String participantEmail;
	
	@Column(nullable = false)
	private BigDecimal deposit;

	private Instant joinedAt;
	
	@PrePersist
    void onCreate() {
        joinedAt = Instant.now();
    }

	public AuctionParticipantModel() {
		super();
	}

	public AuctionParticipantModel(int auctionId, String participantEmail, BigDecimal deposit) {
		super();
		this.auctionId = auctionId;
		this.participantEmail = participantEmail;
		this.deposit = deposit;
	}



	public AuctionParticipantModel(int id, int auctionId, String participantEmail, BigDecimal deposit) {
		super();
		this.id = id;
		this.auctionId = auctionId;
		this.participantEmail = participantEmail;
		this.deposit = deposit;
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

	public String getParticipantEmail() {
		return participantEmail;
	}

	public void setParticipantEmail(String participantEmail) {
		this.participantEmail = participantEmail;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}

	public Instant getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Instant joinedAt) {
		this.joinedAt = joinedAt;
	}
	
}
