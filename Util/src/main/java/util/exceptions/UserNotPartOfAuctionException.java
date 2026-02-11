package util.exceptions;

public class UserNotPartOfAuctionException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UserNotPartOfAuctionException() {
		
	}
	
	public UserNotPartOfAuctionException(String message) {
		super(message);
	}

}
