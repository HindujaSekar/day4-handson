package com.training.springbootusecase.util;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.training.springbootusecase.exceptions.AuthenticationException;
import com.training.springbootusecase.exceptions.ErrorDto;
import com.training.springbootusecase.exceptions.NoSuchAccountException;
import com.training.springbootusecase.exceptions.NotSufficientFundException;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class AccountExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(NoSuchAccountException.class)
	protected ResponseEntity<ErrorDto> handleWhenNoAccountFound(RuntimeException e){
		return new ResponseEntity<>(ErrorDto
				.builder()
				.errorCode(404)
				.errorMessage(e.getMessage())
				.build(), HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<ErrorDto> handleWhenPasswordIsWrong(RuntimeException e){
		return new ResponseEntity<>(ErrorDto
				.builder()
				.errorCode(500)
				.errorMessage(e.getMessage())
				.build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(NotSufficientFundException.class)
	protected ResponseEntity<ErrorDto> handleWhenBalanceIsLow(RuntimeException e){
		return new ResponseEntity<>(ErrorDto
				.builder()
				.errorCode(500)
				.errorMessage(e.getMessage())
				.build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<ErrorDto> handleWhileValidating(RuntimeException e){
		return new ResponseEntity<>(ErrorDto
				.builder()
				.errorCode(500)
				.errorMessage(e.getMessage())
				.build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
