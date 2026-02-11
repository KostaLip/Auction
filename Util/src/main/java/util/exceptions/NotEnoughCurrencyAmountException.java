package util.exceptions;

public class NotEnoughCurrencyAmountException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public NotEnoughCurrencyAmountException() {
		
	}
	
	public NotEnoughCurrencyAmountException(String message) {
		super(message);
	}

}
