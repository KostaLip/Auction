package util.exceptions;

public class CurrencyLowerZeroException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CurrencyLowerZeroException() {
		
	}
	
	public CurrencyLowerZeroException(String message) {
		super(message);
	}

}
