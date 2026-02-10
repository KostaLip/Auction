package util.exceptions;

public class CurrencyNameException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CurrencyNameException() {
		
	}
	
	public CurrencyNameException(String message) {
		super(message);
	}
}
