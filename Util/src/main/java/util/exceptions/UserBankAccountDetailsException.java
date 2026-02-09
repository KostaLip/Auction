package util.exceptions;

public class UserBankAccountDetailsException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UserBankAccountDetailsException() {
		
	}
	
	public UserBankAccountDetailsException(String message) {
		super(message);
	}

}
