package com.example.baristamatic.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.baristamatic.service.exception.DuplicateDrinkNameException;
import com.example.baristamatic.service.exception.DuplicateIngredientNameException;
import com.example.baristamatic.service.exception.InsufficientIngredientsException;

@ControllerAdvice
public class BaristamaticExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		String userMessage = messageSource.getMessage("invalid.message", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.getCause() != null ?  ex.getCause().toString() : ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(userMessage, developerMessage));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		List<Erro> erros = createErroList(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		List<Erro> erros = createErroList(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		String userMessage = messageSource.getMessage("resource.not.found", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(userMessage, developerMessage));
		
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(DuplicateDrinkNameException.class)
	public ResponseEntity<Object> handleDuplicateDrinkNameException(DuplicateDrinkNameException ex){
		String userMessage = messageSource.getMessage("drink.duplicate-name", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(userMessage, developerMessage));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@ExceptionHandler(DuplicateIngredientNameException.class)
	public ResponseEntity<Object> handleDuplicateIngredientNameException(DuplicateIngredientNameException ex){
		String userMessage = messageSource.getMessage("ingredient.duplicate-name", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(userMessage, developerMessage));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@ExceptionHandler(InsufficientIngredientsException.class)
	public ResponseEntity<Object> handleInsufficientIngredientsException(InsufficientIngredientsException ex){
		String userMessage = messageSource.getMessage("ingredient.insufficient-amounts-ingredients", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(userMessage, developerMessage));
		return ResponseEntity.badRequest().body(erros);
	}
	
	private List<Erro> createErroList(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<Erro>();
		
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldError.toString();
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}
			
		return erros;
	}

	public static class Erro {
		
		private String userMessage;
		private String developerMessage;
		
		public Erro (String userMessage, String developerMessage) {
			this.userMessage = userMessage;
			this.developerMessage = developerMessage;
			
		}
		
		public String getUserMessage() {
			return userMessage;
		}
		public String getDeveloperMessage() {
			return developerMessage;
		}
		
	}
}
