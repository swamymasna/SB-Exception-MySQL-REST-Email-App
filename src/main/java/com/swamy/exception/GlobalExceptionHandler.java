package com.swamy.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.swamy.payload.ErrorDetails;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest webRequest) {

		ErrorDetails errorDetails = ErrorDetails.builder().message(exception.getMessage()).status(HttpStatus.NOT_FOUND)
				.timeStamp(LocalDateTime.now()).path(webRequest.getDescription(false)).build();

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
}
