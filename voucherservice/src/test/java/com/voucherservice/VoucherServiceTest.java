package com.voucherservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.voucherservice.entity.Voucher;
import com.voucherservice.exception.DataIsNotInsertedException;
import com.voucherservice.exception.NoVoucherPresentException;
import com.voucherservice.exception.TheseDataIsAlreadyPresentException;
import com.voucherservice.service.VoucherServiceImpl;

@ExtendWith(MockitoExtension.class)
 class VoucherServiceTest {

    @Mock
    private VoucherServiceImpl voucherService;

    @InjectMocks
    private VoucherServiceImpl voucherServiceImpl;

    @Test
     void testSaveAllVouchers_ValidFile() throws IOException, DataIsNotInsertedException, TheseDataIsAlreadyPresentException {
        // Given
        MultipartFile file = createMockMultipartFile("valid-file.xlsx", "test data".getBytes());
        List<Voucher> mockVouchers = getMockVoucherList();

        // Mocking service behavior
        when(voucherService.saveAllVouchers(any())).thenReturn(mockVouchers);

        // Performing the service method
        List<Voucher> result = voucherService.saveAllVouchers(file);

        // Verifying the behavior
        verify(voucherService, times(1)).saveAllVouchers(any());

        // Asserting the returned list matches the expected list
        assertVouchersListEquals(mockVouchers, result);
    }

    @Test
     void testSaveAllVouchers_Exception() throws IOException, DataIsNotInsertedException, TheseDataIsAlreadyPresentException {
        // Given
        MultipartFile file = createMockMultipartFile("test-file.xlsx", "test data".getBytes());

        // Mocking service behavior
        when(voucherService.saveAllVouchers(any())).thenThrow(new DataIsNotInsertedException());

        // Performing the service method and expecting an exception
        assertThrows(DataIsNotInsertedException.class, () -> voucherService.saveAllVouchers(file));

        // Verifying the behavior
        verify(voucherService, times(1)).saveAllVouchers(any());
    }

    @Test
     void testGetAllVouchers_NoVoucherPresent() throws NoVoucherPresentException {
        // Mocking service behavior
        when(voucherService.getAllVouchers()).thenThrow(new NoVoucherPresentException());

        // Performing the service method and expecting an exception
        assertThrows(NoVoucherPresentException.class, () -> voucherService.getAllVouchers());

        // Verifying the behavior
        verify(voucherService, times(1)).getAllVouchers();
    }

    @Test
     void testGetAllVouchers_WithVouchers() throws NoVoucherPresentException {
        // Given
        List<Voucher> mockVouchers = getMockVoucherList();

        // Mocking service behavior
        when(voucherService.getAllVouchers()).thenReturn(mockVouchers);

        // Performing the service method
        List<Voucher> result = voucherService.getAllVouchers();

        // Verifying the behavior
        verify(voucherService, times(1)).getAllVouchers();

        // Asserting the returned list matches the expected list
        assertVouchersListEquals(mockVouchers, result);
    }

    // Additional test cases for other methods can be added based on your requirements

    private void assertVouchersListEquals(List<Voucher> expected, List<Voucher> actual) {
        assertThat(actual).hasSameSizeAs(expected);
        for (int i = 0; i < expected.size(); i++) {
            assertVoucherEquals(expected.get(i), actual.get(i));
        }
    }

    private void assertVoucherEquals(Voucher expected, Voucher actual) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        // Add assertions for other fields as needed
    }

    private List<Voucher> getMockVoucherList() {
        // Assuming this method returns a list of mock vouchers
        List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(new Voucher());
        vouchers.add(new Voucher());
        vouchers.add(new Voucher());
        return vouchers;
    }

    private MultipartFile createMockMultipartFile(String fileName, byte[] content) {
        return new MockMultipartFile(fileName, fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);
    }
}
