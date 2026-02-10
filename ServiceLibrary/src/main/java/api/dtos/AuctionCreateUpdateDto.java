package api.dtos;

import java.math.BigDecimal;

import api.enums.Currency;

public class AuctionCreateUpdateDto {

	private int productId;
	private String ownerEmail;
	private BigDecimal startPrice;
	private Currency currency;
	
	public AuctionCreateUpdateDto() {
		
	}
	
	public AuctionCreateUpdateDto(int productId, String ownerEmail, BigDecimal startPrice, Currency currency) {
		super();
		this.productId = productId;
		this.ownerEmail = ownerEmail;
		this.startPrice = startPrice;
		this.currency = currency;
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

}
