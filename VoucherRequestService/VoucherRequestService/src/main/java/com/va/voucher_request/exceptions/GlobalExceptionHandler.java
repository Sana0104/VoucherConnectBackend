package com.va.voucher_request.exceptions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.va.voucher_request.model.VoucherRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ScoreNotValidException.class)
	public ResponseEntity<String> handleScoreNotValidException(ScoreNotValidException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(VoucherNotFoundException.class)
	public ResponseEntity<Object> handleVoucherNotFoundException(VoucherNotFoundException ex) {
	    String responseBody = "Voucher Not Found With This Id: " + ex.getMessage(); // Include exception message for additional context
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
	}
	
	@ExceptionHandler(ParticularVoucherIsAlreadyAssignedException.class)
	public ResponseEntity<String> handleParticularVoucherIsAlreadyAssignedException(ParticularVoucherIsAlreadyAssignedException ex) {
		return new ResponseEntity<>("Particular Voucher Is Already Assigned to Other,Can't Use it", HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(VoucherIsAlreadyAssignedException.class)
	public ResponseEntity<String> handleVoucherIsAlreadyAssignedException(VoucherIsAlreadyAssignedException ex) {
		return new ResponseEntity<>("Voucher is Already Assigned", HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(NoVoucherPresentException.class)
    public ResponseEntity<Object> handleNoVoucherPresentException(NoVoucherPresentException ex) {
        String responseBody = "No Voucher Present: " + ex.getMessage(); // Include exception message for additional context
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex , WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), ex.getMessage(), request.getDescription(false), "Not Found");
		return new ResponseEntity<>(exp,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> handleResourceAlreadyExistException(ResourceAlreadyExistException ex , WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), ex.getMessage(), request.getDescription(false), "Already Exist");
		return new ResponseEntity<>(exp,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(NoCandidateToUpDateException.class)
	public ResponseEntity<ExceptionResponse> handleNoCandidateToUpDateException(NoCandidateToUpDateException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "No changes found to add or update data in the database", request.getDescription(false), "Not Found");
//		log.error("No changes found to add or update data in the database");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(NoCandidatePresentException.class)
	public ResponseEntity<ExceptionResponse> handleNoCandidatePresentException(NoCandidatePresentException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "No Candidates found", request.getDescription(false), "Not Found");
//		log.error("No Candidates found");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(GivenFileIsNotExcelFileException.class)
	public ResponseEntity<ExceptionResponse> handleGivenFileIsNotExcelFileException(GivenFileIsNotExcelFileException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "Choosen File is not an Excel file", request.getDescription(false), "Not Found");
//		log.error("Choosen File is not an Excel file");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(CandidateIsNotEligibleException.class)
	public ResponseEntity<ExceptionResponse> handleCandidateIsNotEligibleException(CandidateIsNotEligibleException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "You are not eligible for the voucher as you have resigned.", request.getDescription(false), "Not Found");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CandidateNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleCandidateNotFoundException(CandidateNotFoundException ex,WebRequest request)
	{
		ExceptionResponse exp = new ExceptionResponse(LocalDate.now(), "You are not eligible for the voucher because you do not belong to this Business Unit.", request.getDescription(false), "Not Found");
		return new ResponseEntity<ExceptionResponse>(exp, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ExamNotPassedException.class)
	public ResponseEntity<String> handleExamNotPassedException(ExamNotPassedException  ex ) {
		return new ResponseEntity<>("Exam result status is not 'Pass'.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NoCompletedVoucherRequestException.class)
    public ResponseEntity<Object> handleNoCompletedVoucherRequestException(NoCompletedVoucherRequestException ex) {
        return ResponseEntity.ok().body(new ArrayList<VoucherRequest>());
    }
	
	@ExceptionHandler(WrongOptionSelectedException.class)
	public ResponseEntity<String> handleWrongOptionSelectedException(WrongOptionSelectedException  ex ) {
		return new ResponseEntity<>("Wrong option selected", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
