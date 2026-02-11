package util.exceptions;

public class CurrencyDepositException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CurrencyDepositException() {
		
	}
	
	public CurrencyDepositException(String message) {
		super(message);
	}

}
