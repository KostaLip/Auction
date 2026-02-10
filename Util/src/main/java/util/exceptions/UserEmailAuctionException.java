package util.exceptions;

public class UserEmailAuctionException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UserEmailAuctionException() {
		
	}
	
	public UserEmailAuctionException(String message) {
		super(message);
	}

}
