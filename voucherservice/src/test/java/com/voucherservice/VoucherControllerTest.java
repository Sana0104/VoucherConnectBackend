package com.voucherservice;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import com.voucherservice.controller.VoucherController;
import com.voucherservice.entity.Voucher;
import com.voucherservice.exception.GivenFileIsNotExcelFileException;
import com.voucherservice.exception.NoVoucherPresentException;
import com.voucherservice.service.VoucherService;

@WebMvcTest(VoucherController.class)
public class VoucherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoucherService voucherService;

    @Test
    public void testAddAllVouchers_ValidFile() throws Exception {
        // Mocking the service method
        List<Voucher> mockVouchers = Arrays.asList(
                new Voucher("AWS", "Certification", "ABC123", LocalDate.now(), LocalDate.now().plusDays(30)),
                new Voucher("Azure", "Certification", "XYZ456", LocalDate.now(), LocalDate.now().plusDays(30))
        );
        Mockito.when(voucherService.saveAllVouchers(Mockito.any(MultipartFile.class))).thenReturn(mockVouchers);

        // Performing the request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/voucher/addVouchers")
                .file(new MockMultipartFile("file", "test-file.xlsx", "multipart/form-data", "test data".getBytes()))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));

        // Verifying that the service method was called
        Mockito.verify(voucherService, Mockito.times(1)).saveAllVouchers(Mockito.any(MultipartFile.class));
    }

    @Test
    public void testAddAllVouchers_InvalidFile() throws Exception {
        // Mocking the service method to throw GivenFileIsNotExcelFileException
        Mockito.when(voucherService.saveAllVouchers(Mockito.any(MultipartFile.class)))
                .thenThrow(new GivenFileIsNotExcelFileException());

        // Performing the request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/voucher/addVouchers")
                .file(new MockMultipartFile("file", "invalid-file.txt", "multipart/form-data", "test data".getBytes()))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        // Verifying that the service method was called
        Mockito.verify(voucherService, Mockito.times(1)).saveAllVouchers(Mockito.any(MultipartFile.class));
    }

    @Test
    public void testGetAllAvailableVouchers() throws Exception {
        // Mocking the service method
        List<Voucher> mockVouchers = Arrays.asList(
                new Voucher("AWS", "Certification", "ABC123", LocalDate.now(), LocalDate.now().plusDays(30)),
                new Voucher("Azure", "Certification", "XYZ456", LocalDate.now(), LocalDate.now().plusDays(30))
        );
        Mockito.when(voucherService.getAllVouchers()).thenReturn(mockVouchers);

        // Performing the request
        mockMvc.perform(MockMvcRequestBuilders.get("/voucher/getAllVouchers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));

        // Verifying that the service method was called
        Mockito.verify(voucherService, Mockito.times(1)).getAllVouchers();
    }

    @Test
    public void testGetAllAvailableVouchers_NoVoucherPresent() throws Exception {
        // Mocking the service method to throw NoVoucherPresentException
        Mockito.when(voucherService.getAllVouchers()).thenThrow(new NoVoucherPresentException());

        // Performing the request
        mockMvc.perform(MockMvcRequestBuilders.get("/voucher/getAllVouchers"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verifying that the service method was called
        Mockito.verify(voucherService, Mockito.times(1)).getAllVouchers();
    }

    // Additional test cases for other controller methods can be added similarly
}
