package util.exceptions;

public class UserProfileDetailsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UserProfileDetailsException() {
		
	}

	public UserProfileDetailsException(String message) {
		super(message);
	}
	
}
