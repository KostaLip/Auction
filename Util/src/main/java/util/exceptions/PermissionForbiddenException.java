package util.exceptions;

public class PermissionForbiddenException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PermissionForbiddenException() {
		
	}
	
	public PermissionForbiddenException(String message) {
		super(message);
	}

}
