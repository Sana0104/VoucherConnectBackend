package com.voucherservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.voucherservice.entity.Voucher;

     class VoucherTest {

        @Test
         void testParameterizedConstructor() {
            LocalDate issuedDate = LocalDate.now();
            LocalDate expiryDate = LocalDate.now().plusMonths(1);

            Voucher voucher = new Voucher("AWS", "Certification Exam", "ABC123", issuedDate, expiryDate);

            assertEquals("AWS", voucher.getCloudPlatform());
            assertEquals("Certification Exam", voucher.getExamName());
            assertEquals("ABC123", voucher.getVoucherCode());
            assertEquals(issuedDate, voucher.getIssuedDate());
            assertEquals(expiryDate, voucher.getExpiryDate());
            assertNull(voucher.getIssuedTo()); // IssuedTo should be null initially
        }

        @Test
         void testEqualsAndHashCode() {
            LocalDate issuedDate = LocalDate.now();
            LocalDate expiryDate = LocalDate.now().plusMonths(1);

            Voucher voucher1 = new Voucher("AWS", "Certification Exam", "ABC123", issuedDate, expiryDate);
            Voucher voucher2 = new Voucher("AWS", "Certification Exam", "ABC123", issuedDate, expiryDate);
            Voucher voucher3 = new Voucher("Azure", "Java Exam", "XYZ789", issuedDate, expiryDate);

            // Test equals
            assertEquals(voucher1, voucher2);
            assertNotEquals(voucher1, voucher3);

            // Test hashCode
            assertEquals(voucher1.hashCode(), voucher2.hashCode());
            assertNotEquals(voucher1.hashCode(), voucher3.hashCode());
        }

        @Test
         void testSetterAndGetters() {
            Voucher voucher = new Voucher();
            LocalDate newIssuedDate = LocalDate.now();
            LocalDate newExpiryDate = LocalDate.now().plusMonths(2);

            voucher.setCloudPlatform("GCP");
            voucher.setExamName("Data Engineer Exam");
            voucher.setVoucherCode("XYZ789");
            voucher.setIssuedDate(newIssuedDate);
            voucher.setExpiryDate(newExpiryDate);
            voucher.setIssuedTo("user@example.com");

            assertEquals("GCP", voucher.getCloudPlatform());
            assertEquals("Data Engineer Exam", voucher.getExamName());
            assertEquals("XYZ789", voucher.getVoucherCode());
            assertEquals(newIssuedDate, voucher.getIssuedDate());
            assertEquals(newExpiryDate, voucher.getExpiryDate());
            assertEquals("user@example.com", voucher.getIssuedTo());
        }

        @Test
         void testToString() {
            LocalDate issuedDate = LocalDate.now();
            LocalDate expiryDate = LocalDate.now().plusMonths(1);

            Voucher voucher = new Voucher("Azure", "Java Exam", "XYZ789", issuedDate, expiryDate);
            String toStringResult = voucher.toString();

            assertThat(toStringResult).isNotNull();
            assertThat(toStringResult).contains("Azure", "Java Exam", "XYZ789", issuedDate.toString(), expiryDate.toString());
        }

        // Add more test cases as needed to cover other methods or scenarios
    }



