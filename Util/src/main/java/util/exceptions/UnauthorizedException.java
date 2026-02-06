package util.exceptions;

public class UnauthorizedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException() {
		
	}
	
	public UnauthorizedException(String message) {
		super(message);
	}

}
