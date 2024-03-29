package com.va.voucher_request.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.va.voucher_request.exceptions.ExamNotPassedException;
import com.va.voucher_request.exceptions.NoCompletedVoucherRequestException;
import com.va.voucher_request.exceptions.NoVoucherPresentException;
import com.va.voucher_request.exceptions.NotAnImageFileException;
import com.va.voucher_request.exceptions.NotFoundException;
import com.va.voucher_request.exceptions.ParticularVoucherIsAlreadyAssignedException;
import com.va.voucher_request.exceptions.ResourceAlreadyExistException;
import com.va.voucher_request.exceptions.ScoreNotValidException;
import com.va.voucher_request.exceptions.VoucherIsAlreadyAssignedException;
import com.va.voucher_request.exceptions.VoucherNotFoundException;
import com.va.voucher_request.exceptions.WrongOptionSelectedException;
import com.va.voucher_request.model.VoucherRequest;
import com.va.voucher_request.model.VoucherRequestDto;

import jakarta.mail.MessagingException;

public interface VoucherReqService {

	VoucherRequest requestVoucher(VoucherRequestDto request, MultipartFile file, String path)
			throws ScoreNotValidException, ResourceAlreadyExistException, NotAnImageFileException, IOException;

	List<VoucherRequest> getAllVouchersByCandidateEmail(String candidateEmail) throws NotFoundException;

	VoucherRequest updateExamDate(String voucherCode, LocalDate newExamDate) throws NotFoundException;

	VoucherRequest updateExamResult(String voucherCode, String newExamResult) throws NotFoundException;

	VoucherRequest assignVoucher(String voucherId, String emailId, String voucherrequestId)
			throws VoucherNotFoundException, NotFoundException, VoucherIsAlreadyAssignedException,
			ParticularVoucherIsAlreadyAssignedException, MessagingException;

	List<VoucherRequest> getAllVoucherRequest() throws VoucherNotFoundException;

	List<VoucherRequest> getAllAssignedVoucherRequest() throws NoVoucherPresentException;

	List<VoucherRequest> getAllNotAssignedVoucherRequest() throws NoVoucherPresentException;

	List<VoucherRequest> getAllCompletedVoucherRequest() throws NoCompletedVoucherRequestException;

	List<String> pendingEmails();

	List<VoucherRequest> pendingRequests();

	VoucherRequest uploadCertificate(String voucherCode, MultipartFile certificateFile, String path)
			throws ExamNotPassedException, IOException, NotAnImageFileException, NotFoundException;

	VoucherRequest denyRequest(String requestId, String reason) throws NoVoucherPresentException;

	String getValidationNumber(String voucherRequestId) throws NotFoundException;

	VoucherRequest uploadR2d2Screenshot(String voucherCode, MultipartFile screenshot, String path)
			throws WrongOptionSelectedException, IOException, NotAnImageFileException, NotFoundException;

	VoucherRequest updateField(String voucherRequestId, Map<String, Object> updates)
			throws NotFoundException, NoSuchFieldException, IllegalAccessException;

	void provideValidationNumber(String voucherRequestId, String validationNumber) throws NotFoundException;

	List<VoucherRequest> getTotalResignedCandidateRequest();

	List<VoucherRequest> getTotalBUChangeCandidateCount();

}
