package util.exceptions;

public class HighestBidException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public HighestBidException() {
		
	}
	
	public HighestBidException(String message) {
		super(message);
	}

}
