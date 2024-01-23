package com.voucherservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.voucherservice.entity.Voucher;
import com.voucherservice.repository.VoucherRepository;

@DataMongoTest
public class VoucherRepositoryTest {

    @Autowired
    private VoucherRepository voucherRepository;

    @Test
    public void testFindByExamName() {
        // Given
        String examName = "MockExam";
        List<Voucher> mockVouchers = getMockVoucherList();
        voucherRepository.saveAll(mockVouchers);

        // When
        List<Voucher> result = voucherRepository.findByExamName(examName);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(mockVouchers);
    }

    @Test
    public void testFindByCloudPlatform() {
        // Given
        String cloudPlatform = "MockPlatform";
        List<Voucher> mockVouchers = getMockVoucherList();
        voucherRepository.saveAll(mockVouchers);

        // When
        List<Voucher> result = voucherRepository.findByCloudPlatform(cloudPlatform);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(mockVouchers);
    }

    // Add more test cases for other methods in the VoucherRepository interface

    private List<Voucher> getMockVoucherList() {
        List<Voucher> vouchers = new ArrayList<>();
        vouchers.add(new Voucher("Cloud1", "Exam1", "Voucher1", LocalDate.now(), LocalDate.now().plusDays(30)));
        vouchers.add(new Voucher("Cloud2", "Exam2", "Voucher2", LocalDate.now(), LocalDate.now().plusDays(30)));
        return vouchers;
    }
}
