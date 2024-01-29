package com.va.voucher_request.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

 class VoucherTest {

    @Test
     void testCreateVoucher() {
        // Arrange
        String id = "1";
        String cloudPlatform = "AWS";
        String examName = "Java Certification";
        String voucherCode = "ABC123";
        LocalDate issuedDate = LocalDate.of(2024, 1, 1);
        LocalDate expiryDate = LocalDate.of(2025, 1, 1);
        String issuedTo = "John Doe";

        // Act
        Voucher voucher = new Voucher(id, cloudPlatform, examName, voucherCode, issuedDate, expiryDate, issuedTo);

        // Assert
        assertNotNull(voucher);
        assertEquals(id, voucher.getId());
        assertEquals(cloudPlatform, voucher.getCloudPlatform());
        assertEquals(examName, voucher.getExamName());
        assertEquals(voucherCode, voucher.getVoucherCode());
        assertEquals(issuedDate, voucher.getIssuedDate());
        assertEquals(expiryDate, voucher.getExpiryDate());
        assertEquals(issuedTo, voucher.getIssuedTo());
    }

    @Test
     void testVoucherSetter() {
        // Arrange
        Voucher voucher = new Voucher();
        String newIssuedTo = "Jane Doe";

        // Act
        voucher.setIssuedTo(newIssuedTo);

        // Assert
        assertEquals(newIssuedTo, voucher.getIssuedTo());
    }

    @Test
     void testVoucherToString() {
        // Arrange
        Voucher voucher = new Voucher("1", "AWS", "Java Certification", "ABC123",
                LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "John Doe");

        // Act
        String voucherString = voucher.toString();

        // Assert
        assertNotNull(voucherString);
    }
    
    @Test
     void testEqualsAndHashCode() {
        // Arrange
        Voucher voucher1 = new Voucher("1", "AWS", "Java Certification", "ABC123",
                LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "John Doe");

        Voucher voucher2 = new Voucher("1", "AWS", "Java Certification", "ABC123",
                LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "John Doe");

        // Act & Assert
        assertEquals(voucher1, voucher2);
        assertEquals(voucher1.hashCode(), voucher2.hashCode());
    }

    @Test
     void testNotEquals() {
        // Arrange
        Voucher voucher1 = new Voucher("1", "AWS", "Java Certification", "ABC123",
                LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "John Doe");

        Voucher voucher2 = new Voucher("2", "Azure", "Python Certification", "XYZ789",
                LocalDate.of(2024, 2, 1), LocalDate.of(2025, 2, 1), "Jane Doe");

        // Act & Assert
        assertNotEquals(voucher1, voucher2);
        assertNotEquals(voucher1.hashCode(), voucher2.hashCode());
    }


    @Test
     void testAllArgsConstructor() {
        // Arrange
        String id = "1";
        String cloudPlatform = "AWS";
        String examName = "Java Certification";
        String voucherCode = "ABC123";
        LocalDate issuedDate = LocalDate.of(2024, 1, 1);
        LocalDate expiryDate = LocalDate.of(2025, 1, 1);
        String issuedTo = "John Doe";

        // Act
        Voucher voucher = new Voucher(id, cloudPlatform, examName, voucherCode, issuedDate, expiryDate, issuedTo);

        // Assert
        assertNotNull(voucher);
        assertEquals(id, voucher.getId());
        assertEquals(cloudPlatform, voucher.getCloudPlatform());
        assertEquals(examName, voucher.getExamName());
        assertEquals(voucherCode, voucher.getVoucherCode());
        assertEquals(issuedDate, voucher.getIssuedDate());
        assertEquals(expiryDate, voucher.getExpiryDate());
        assertEquals(issuedTo, voucher.getIssuedTo());
    }

    @Test
     void testNoArgsConstructor() {
        // Act
        Voucher voucher = new Voucher();

        // Assert
        assertNotNull(voucher);
        assertNull(voucher.getId());
        // Repeat for other properties
    }

    @Test
     void testToString() {
        // Arrange
        Voucher voucher = new Voucher("1", "AWS", "Java Certification", "ABC123",
                LocalDate.of(2024, 1, 1), LocalDate.of(2025, 1, 1), "John Doe");

        // Act
        String toStringResult = voucher.toString();

        // Assert
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("AWS"));
        assertTrue(toStringResult.contains("Java Certification"));
        assertTrue(toStringResult.contains("John Doe"));
    }

    @Test
     void testGetterSetterGenerated() {
        // Arrange
        Voucher voucher = new Voucher();

        // Act & Assert
        voucher.setId("1");
        assertEquals("1", voucher.getId());

        voucher.setCloudPlatform("AWS");
        assertEquals("AWS", voucher.getCloudPlatform());

        // Repeat for other properties
    }


}
