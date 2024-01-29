package com.voucherservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.voucherservice.dto.VoucherRequest;

 class VoucherRequestTest {

    @Test
     void testDefaultConstructor() {
        // Given
        VoucherRequest voucherRequest = new VoucherRequest();

        // Then
        assertThat(voucherRequest).isNotNull();
        assertThat(voucherRequest.getId()).isNull();
        // Add assertions for other fields
    }

    @Test
     void testEqualsAndHashCode() {
        // Given
        VoucherRequest voucherRequest1 = new VoucherRequest();
        voucherRequest1.setId("123");

        VoucherRequest voucherRequest2 = new VoucherRequest();
        voucherRequest2.setId("123");

        VoucherRequest voucherRequest3 = new VoucherRequest();
        voucherRequest3.setId("456");

        // Then
        assertThat(voucherRequest1).isEqualTo(voucherRequest2);
        assertThat(voucherRequest1.hashCode()).isEqualTo(voucherRequest2.hashCode());
        assertThat(voucherRequest1).isNotEqualTo(voucherRequest3);
        assertThat(voucherRequest1.hashCode()).isNotEqualTo(voucherRequest3.hashCode());
    }

    @Test
     void testSetterWithChaining() {
        // Given
        VoucherRequest voucherRequest = new VoucherRequest();

        // When
       
        // Then
        assertThat(voucherRequest.getId()).isEqualTo("123");
        assertThat(voucherRequest.getCandidateName()).isEqualTo("John Doe");
        assertThat(voucherRequest.getCandidateEmail()).isEqualTo("john.doe@example.com");
        // Add assertions for other fields
    }

    // Add more test cases as needed

    @Test
     void testGetterSetter() {
        // Given
        VoucherRequest voucherRequest = new VoucherRequest();

        // When
        voucherRequest.setId("123");
        voucherRequest.setCandidateName("John Doe");
        voucherRequest.setCandidateEmail("john.doe@example.com");
        voucherRequest.setCloudPlatform("MockPlatform");
        voucherRequest.setCloudExam("MockExam");
        voucherRequest.setDoSelectScore(90);
        voucherRequest.setDoSelectScoreImage("scoreImage.png");
        voucherRequest.setVoucherCode("Voucher123");
        voucherRequest.setVoucherIssueLocalDate(LocalDate.of(2022, 1, 1));
        voucherRequest.setVoucherExpiryLocalDate(LocalDate.of(2022, 12, 31));
        voucherRequest.setPlannedExamDate(LocalDate.of(2022, 6, 15));
        voucherRequest.setExamResult("Pass");

        // Then
        assertThat(voucherRequest.getId()).isEqualTo("123");
        assertThat(voucherRequest.getCandidateName()).isEqualTo("John Doe");
        assertThat(voucherRequest.getCandidateEmail()).isEqualTo("john.doe@example.com");
        assertThat(voucherRequest.getCloudPlatform()).isEqualTo("MockPlatform");
        assertThat(voucherRequest.getCloudExam()).isEqualTo("MockExam");
        assertThat(voucherRequest.getDoSelectScore()).isEqualTo(90);
        assertThat(voucherRequest.getDoSelectScoreImage()).isEqualTo("scoreImage.png");
        assertThat(voucherRequest.getVoucherCode()).isEqualTo("Voucher123");
        assertThat(voucherRequest.getVoucherIssueLocalDate()).isEqualTo(LocalDate.of(2022, 1, 1));
        assertThat(voucherRequest.getVoucherExpiryLocalDate()).isEqualTo(LocalDate.of(2022, 12, 31));
        assertThat(voucherRequest.getPlannedExamDate()).isEqualTo(LocalDate.of(2022, 6, 15));
        assertThat(voucherRequest.getExamResult()).isEqualTo("Pass");
    }

    @Test
     void testToString() {
        // Given
        VoucherRequest voucherRequest = new VoucherRequest();

        // When
        String result = voucherRequest.toString();

        // Then
        assertThat(result).contains("John Doe", "MockPlatform", "Voucher123", "2022-01-01", "2022-12-31");
    }
}
