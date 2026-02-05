package util.exceptions;

public class DeletingOtherUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DeletingOtherUserException() {
		
	}
	
	public DeletingOtherUserException(String message) {
		super(message);
	}

}
