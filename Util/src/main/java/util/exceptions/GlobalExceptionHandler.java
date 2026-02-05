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
