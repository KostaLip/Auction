package util.exceptions;

public class CreateProductUserException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CreateProductUserException() {
		
	}
	
	public CreateProductUserException(String message) {
		super(message);
	}

}
