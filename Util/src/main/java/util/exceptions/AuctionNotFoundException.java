package util.exceptions;

public class AuctionNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AuctionNotFoundException() {
		
	}
	
	public AuctionNotFoundException(String message) {
		super(message);
	}

}
