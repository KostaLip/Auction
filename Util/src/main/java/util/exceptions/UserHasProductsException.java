package util.exceptions;

public class UserHasProductsException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UserHasProductsException() {
		
	}
	
	public UserHasProductsException(String message) {
		super(message);
	}

}
