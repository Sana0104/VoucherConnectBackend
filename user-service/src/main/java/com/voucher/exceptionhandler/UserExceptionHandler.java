package com.voucher.exceptionhandler;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.voucher.exceptions.CandidateIsNotEligibleException;
import com.voucher.exceptions.GivenFileIsNotExcelFileException;
import com.voucher.exceptions.NoCandidatePresentException;
import com.voucher.exceptions.NoCandidateToUpDateException;
import com.voucher.exceptions.NotAnImageFileException;
import com.voucher.exceptions.ResourceAlreadyExistException;
import com.voucher.exceptions.ResourceNotFoundException;
import com.voucher.exceptions.UserAlreadyExistException;
import com.voucher.exceptions.UserIsNotPresentWithEmailException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> handleUserAlreadyExistException(UserAlreadyExistException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "User Already Exist", request.getDescription(false), "Not Acceptable");
		log.error("User Already Exist");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(UserIsNotPresentWithEmailException.class)
	public ResponseEntity<ExceptionResponse> handleUserIsNotPresentWithEmailException(UserIsNotPresentWithEmailException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "User Not Exist with this Email", request.getDescription(false), "Not Found");
		log.error("User Not Present with this email");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoCandidateToUpDateException.class)
	public ResponseEntity<ExceptionResponse> handleNoCandidateToUpDateException(NoCandidateToUpDateException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "No changes found to add or update data in the database", request.getDescription(false), "Not Found");
		log.error("No changes found to add or update data in the database");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(NoCandidatePresentException.class)
	public ResponseEntity<ExceptionResponse> handleNoCandidatePresentException(NoCandidatePresentException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "No Candidates found", request.getDescription(false), "Not Found");
		log.error("No Candidates found");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(GivenFileIsNotExcelFileException.class)
	public ResponseEntity<ExceptionResponse> handleGivenFileIsNotExcelFileException(GivenFileIsNotExcelFileException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "Choosen File is not an Excel file", request.getDescription(false), "Not Found");
		log.error("Choosen File is not an Excel file");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(CandidateIsNotEligibleException.class)
	public ResponseEntity<ExceptionResponse> handleCandidateIsNotEligibleException(CandidateIsNotEligibleException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "You are not eligible for the voucher", request.getDescription(false), "Not Found");
		log.error("candidate is not eligible for the voucher");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	
	
	@ExceptionHandler(NotAnImageFileException.class)
	public ResponseEntity<ExceptionResponse> handleNotAnImageFileException(NotAnImageFileException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "Please provide a file with .png or .jpeg or .jpg extension", request.getDescription(false), "Not Found");
		log.error("The particular file is not an image file for uploading");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ExceptionResponse> handleIOException(IOException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "Some error occured during coping the image", request.getDescription(false), "internal server error");
		log.error("Some error occured during coping the image to system folder");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<ExceptionResponse> handleMultipartException(MultipartException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "Choose an Image for Upload", request.getDescription(false), "Not Found");
		log.error("Possibly No image is choosen for uploading ");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), ex.getMessage(), request.getDescription(false), "Not Found");
		log.error("Resource is not found");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> handleResourceAlreadyExistException(ResourceAlreadyExistException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), ex.getMessage(), request.getDescription(false), "Already Exist");
		log.error("Resource is Already exist");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		StringBuilder details = new StringBuilder();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			details.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ");
		}
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDate.now(), "Validation fails",
				details.toString(), "Bad Request");
		log.error("Validation fails:", ex);
//        log.error(ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleUserBadCredentialsException(BadCredentialsException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "Please Give Correct Email and Password", request.getDescription(false), "Not Found");
		log.error("Please Give Correct Email and Password");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}

}
