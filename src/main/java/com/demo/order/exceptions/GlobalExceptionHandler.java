package com.demo.order.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handler for identified exceptions that occur within the application.
 * 
 * @author marlowelandicho
 *
 */
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

	/**
	 * Global Exception handler for all exceptions.
	 */
	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handle(Exception exception) {
		// general exception

		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("error", exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);

	}

	/**
	 * Catches all NPEs that cannot be processed within the application.
	 * 
	 * @param npex exception instance passed
	 * @return error message
	 */
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorDetails> handleNullPointerException(NullPointerException npex) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), "Nothing found", npex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

	}

	/**
	 * Exceptions raised when traversing empty elements.
	 * 
	 * @param nex exception instance passed
	 * @return error message
	 */
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorDetails> handleNoSuchElementException(NoSuchElementException nex) {

		ErrorDetails errorDetails = new ErrorDetails(new Date(), "Nothing found", nex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);

	}

	/**
	 * Validation Exceptions that occurred during the validation phase.
	 * 
	 * @param ex exception instance passed
	 * @return error message
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ErrorDetails>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {

		List<ErrorDetails> errors = new ArrayList<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String errorMessage = error.getDefaultMessage();
			ErrorDetails errorDetails = new ErrorDetails(new Date(), errorMessage, ex.getMessage());
			errors.add(errorDetails);
		});

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);

	}

	/**
	 * Validation Exceptions that occurred during the validation phase.
	 * 
	 * @param ex exception instance passed
	 * @return error message
	 */
	@ExceptionHandler(OrderException.class)
	public ResponseEntity<Map<String, String>> handleOrderException(OrderException oex) {

		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("error", oex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);

	}

}
