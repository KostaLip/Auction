package util.exceptions;

public class ProductOnAuctionException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ProductOnAuctionException() {
		
	}
	
	public ProductOnAuctionException(String message) {
		super(message);
	}

}
