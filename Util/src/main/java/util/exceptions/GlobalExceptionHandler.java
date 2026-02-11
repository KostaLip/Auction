package util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter email of existing user", 
						HttpStatus.NOT_FOUND));
	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter id of existing product", 
						HttpStatus.NOT_FOUND));
	}
	
	@ExceptionHandler(AuctionNotFoundException.class)
	public ResponseEntity<?> handleAuctionNotFoundException(AuctionNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter id of existing auction", 
						HttpStatus.NOT_FOUND));
	}
	
	@ExceptionHandler(UserBankAccountDetailsException.class)
	public ResponseEntity<?> handleUserBankAccountDetailsException(
			UserBankAccountDetailsException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter your email to see bank account details", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(CurrencyLowerZeroException.class)
	public ResponseEntity<?> handleCurrencyLowerZeroException(
			CurrencyLowerZeroException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter valid numbers", 
						HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter valid email and password of existing user", 
						HttpStatus.UNAUTHORIZED));
	}
	
	@ExceptionHandler(UserProfileDetailsException.class)
	public ResponseEntity<?> handleUserProfileDetailsException(UserProfileDetailsException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter your email to see profile details", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(UserEditAnotherUserException.class)
	public ResponseEntity<?> handleUserEditAnotherUserException(UserEditAnotherUserException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter your email to update your profile", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(UserEmailAuctionException.class)
	public ResponseEntity<?> handleUserEmailAuctionException(UserEmailAuctionException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter your email to create auction", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(CreateProductUserException.class)
	public ResponseEntity<?> handleCreateProductUserException(CreateProductUserException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter your email to create/update/delete your product", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(UserHasProductsException.class)
	public ResponseEntity<?> handleUserHasProductsException(UserHasProductsException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ExceptionModel(e.getMessage(), 
						"First delete all of your products", 
						HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(ProductOnAuctionException.class)
	public ResponseEntity<?> handleProductOnAuctionException(ProductOnAuctionException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ExceptionModel(e.getMessage(), 
						"Enter different product id for auction", 
						HttpStatus.BAD_REQUEST));
	}
	
	@ExceptionHandler(CurrencyDepositException.class)
	public ResponseEntity<?> handleCurrencyDepositException(CurrencyDepositException e) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
				new ExceptionModel(e.getMessage(), 
						"Add more currency to your bank account", 
						HttpStatus.UNPROCESSABLE_ENTITY));
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleJsonParseException(HttpMessageNotReadableException e) {
	    
	    if (e.getCause() instanceof InvalidFormatException) {
	        InvalidFormatException ife = (InvalidFormatException) e.getCause();
	        
	        if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	                new ExceptionModel(
	                    "Invalid currency value: " + ife.getValue(),
	                    "Enter currency from the list: USD, EUR, RSD",
	                    HttpStatus.BAD_REQUEST
	                )
	            );
	        }
	    }
	    
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	        new ExceptionModel(
	            "Invalid JSON format",
	            e.getMessage(),
	            HttpStatus.BAD_REQUEST
	        )
	    );
	}
	
	@ExceptionHandler(RoleChangeException.class)
	public ResponseEntity<?> handleRoleChangeException(RoleChangeException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Do not change existing role", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(AuctionOwnerException.class)
	public ResponseEntity<?> handleAuctionOwnerException(AuctionOwnerException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Provide id of one of your products", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(
				new ExceptionModel(e.getMessage(), 
						"Please provide different email", 
						HttpStatus.CONFLICT));
	}
	
	@ExceptionHandler(DeletingOtherUserException.class)
	public ResponseEntity<?> handleDeletingOtherUserException(DeletingOtherUserException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Please provide your email to delete an account", 
						HttpStatus.FORBIDDEN));
	}
	
	@ExceptionHandler(AdminUserDeletedException.class)
	public ResponseEntity<?> handleAdminUserDeletedException(AdminUserDeletedException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Please provide non-admin email", 
						HttpStatus.FORBIDDEN));
	}
	
}
