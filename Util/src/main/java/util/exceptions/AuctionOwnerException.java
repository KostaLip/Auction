package util.exceptions;

public class AuctionOwnerException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public AuctionOwnerException() {
		
	}
	
	public AuctionOwnerException(String message) {
		super(message);
	}

}
