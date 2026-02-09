package bankAccount;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class BankAccountModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "Bank_Account_SEQ_GENERATOR", sequenceName = "bank_account_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Bank_Account_SEQ_GENERATOR")
	private int id;
	
	@Column(unique = true, nullable = false)
	private String email;

	private BigDecimal usdAmount;
	private BigDecimal eurAmount;
	private BigDecimal rsdAmount;
	
	public BankAccountModel() {
		
	}
	
	public BankAccountModel(String email, BigDecimal usdAmount, BigDecimal eurAmount, 
			BigDecimal rsdAmount) {
		this.email = email;
		this.usdAmount = usdAmount;
		this.eurAmount = eurAmount;
		this.rsdAmount = rsdAmount;
	}
	
	public BankAccountModel(int id, String email, BigDecimal usdAmount, 
			BigDecimal eurAmount, BigDecimal rsdAmount) {
		this.id = id;
		this.email = email;
		this.usdAmount = usdAmount;
		this.eurAmount = eurAmount;
		this.rsdAmount = rsdAmount;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public BigDecimal getUsdAmount() {
		return usdAmount;
	}
	
	public void setUsdAmount(BigDecimal usdAmount) {
		this.usdAmount = usdAmount;
	}
	
	public BigDecimal getEurAmount() {
		return eurAmount;
	}
	
	public void setEurAmount(BigDecimal eurAmount) {
		this.eurAmount = eurAmount;
	}
	
	public BigDecimal getRsdAmount() {
		return rsdAmount;
	}
	
	public void setRsdAmount(BigDecimal rsdAmount) {
		this.rsdAmount = rsdAmount;
	}
	
}
