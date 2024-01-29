package com.voucherservice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.voucherservice.client.VoucherRequestClient;
import com.voucherservice.entity.Voucher;
import com.voucherservice.exception.DataIsNotInsertedException;
import com.voucherservice.exception.NoVoucherPresentException;
import com.voucherservice.exception.TheseDataIsAlreadyPresentException;
import com.voucherservice.repository.VoucherRepository;
import com.voucherservice.service.VoucherServiceImpl;

@ExtendWith(MockitoExtension.class)
 class VoucherServiceImplTest {

    @Mock
    private VoucherRepository voucherRepo;

    @Mock
    private VoucherRequestClient reqClient;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @Test
     void testSaveAllVouchers_ValidFile() throws IOException, DataIsNotInsertedException, TheseDataIsAlreadyPresentException {
        // Given
        MultipartFile file = createMockMultipartFile("valid-file.xlsx", "test data".getBytes());
        List<Voucher> mockVouchers = getMockVoucherList();

        // Mocking repository behavior
        when(voucherRepo.saveAll(anyList())).thenReturn(mockVouchers);

        // Performing the service method
        List<Voucher> result = voucherService.saveAllVouchers(file);

        // Verifying the behavior
        verify(voucherRepo, times(1)).saveAll(anyList());

        // Asserting the returned list matches the expected list
        assertVouchersListEquals(mockVouchers, result);
    }

    @Test
     void testSaveAllVouchers_DuplicateData() throws IOException, DataIsNotInsertedException {
        MultipartFile file = createMockMultipartFile("duplicate-file.xlsx", "test data".getBytes());

        // Mocking repository behavior to simulate existing data
        when(voucherRepo.findAll()).thenReturn(getMockVoucherList());

        // Performing the service method and expecting an exception
        assertThrows(TheseDataIsAlreadyPresentException.class, () -> voucherService.saveAllVouchers(file));

        // Verifying the behavior
        verify(voucherRepo, never()).saveAll(anyList());
        verify(voucherRepo, times(1)).findAll();
    }

    @Test
     void testSaveAllVouchers_SaveAllException() throws IOException {
        // Given
        MultipartFile file = createMockMultipartFile("test-file.xlsx", "test data".getBytes());
        List<Voucher> mockVouchers = getMockVoucherList();
        
        // Mocking behavior
        when(voucherRepo.findAll()).thenReturn(new ArrayList<>());
        when(voucherRepo.saveAll(anyList())).thenThrow(new RuntimeException("SaveAll failed"));

        DataIsNotInsertedException exception = assertThrows(DataIsNotInsertedException.class, () -> {
            voucherService.saveAllVouchers(file);
        });

        // Verifying the behavior
        assertThat(exception.getMessage()).isEqualTo("Data could not be inserted.");
        verify(voucherRepo, times(1)).saveAll(anyList());
        verify(voucherRepo, times(1)).findAll();
    }

    @Test
     void testGetAllVouchers_NoVoucherPresent() {
        // Given
        when(voucherRepo.findAll()).thenReturn(new ArrayList<>());

        // Performing the service method and expecting an exception
        assertThrows(NoVoucherPresentException.class, () -> voucherService.getAllVouchers());

        // Verifying the behavior
        verify(voucherRepo, times(1)).findAll();
    }

    @Test
     void testGetAllVouchers_ExpiredVouchers() throws NoVoucherPresentException {
        // Given
        List<Voucher> mockVouchers = getMockVoucherList();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // Modify the expiry date of one voucher to be yesterday
        mockVouchers.get(0).setExpiryDate(yesterday);

        when(voucherRepo.findAll()).thenReturn(mockVouchers);

        // Performing the service method
        List<Voucher> result = voucherService.getAllVouchers();

        // Verifying the behavior
        verify(voucherRepo, times(1)).findAll();

        // Asserting the returned list does not contain expired vouchers
        assertThat(result).hasSize(2); // Assuming 3 vouchers in the list
        assertThat(result).noneMatch(voucher -> voucher.getExpiryDate().isBefore(LocalDate.now()));
    }

    // Additional test cases for other service methods can be added based on your requirements

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
