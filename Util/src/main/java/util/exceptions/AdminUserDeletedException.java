package util.exceptions;

public class AdminUserDeletedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AdminUserDeletedException() {
		
	}
	
	public AdminUserDeletedException(String message) {
		super(message);
	}

}
