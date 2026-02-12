package util.exceptions;

public class AuctionNotActiveException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AuctionNotActiveException() {
		
	}
	
	public AuctionNotActiveException(String message) {
		super(message);
	}

}
