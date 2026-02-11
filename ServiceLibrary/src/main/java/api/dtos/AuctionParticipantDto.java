package api.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public class AuctionParticipantDto {

	private int auctionId;
	private String participantEmail;
	private BigDecimal deposit;
	private Instant joinedAt;
	
	public AuctionParticipantDto() {
		super();
	}

	public AuctionParticipantDto(int auctionId, String participantEmail, BigDecimal deposit, Instant joinedAt) {
		super();
		this.auctionId = auctionId;
		this.participantEmail = participantEmail;
		this.deposit = deposit;
		this.joinedAt = joinedAt;
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
