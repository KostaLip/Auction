package util.exceptions;

public class RoleChangeException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RoleChangeException() {
		
	}
	
	public RoleChangeException(String message) {
		super(message);
	}

}
