package util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ExceptionModel(e.getMessage(), 
						"Please enter email of existing user", 
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
	
	@ExceptionHandler(RoleChangeException.class)
	public ResponseEntity<?> handleRoleChangeException(RoleChangeException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				new ExceptionModel(e.getMessage(), 
						"Do not change existing role", 
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
