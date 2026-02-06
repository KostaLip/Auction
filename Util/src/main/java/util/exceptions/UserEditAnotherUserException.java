package util.exceptions;

public class UserEditAnotherUserException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UserEditAnotherUserException() {
		
	}
	
	public UserEditAnotherUserException(String message) {
		super(message);
	}

}
