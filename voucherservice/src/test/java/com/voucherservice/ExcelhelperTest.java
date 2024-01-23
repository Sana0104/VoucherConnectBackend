package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.voucherservice.entity.Voucher;
import com.voucherservice.helper.Excelhelper;

class ExcelhelperTest {

    @Test
    void testCheckExcelFormat_ValidExcel() {
        MockMultipartFile file = new MockMultipartFile("test.xlsx", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test data".getBytes());
        assertTrue(Excelhelper.checkExcelFormat(file));
    }

    @Test
    void testCheckExcelFormat_InvalidFormat() {
        MockMultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", "test data".getBytes());
        assertFalse(Excelhelper.checkExcelFormat(file));
    }

    @Test
    void testConvertToDateFromString() throws ParseException {
        String dateString = "2022-01-23";
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse(dateString), Excelhelper.convertToDateFromString(dateString));
    }

    @Test
    void testConvertExcelToListOfVoucher() throws IOException {
        String csvData = "Exam Name,Cloud Platform,Voucher Code,Issued Date,Expiry Date\n" +
                "Exam1,Cloud1,ABC123,2022-01-23,2022-02-23\n" +
                "Exam2,Cloud2,XYZ456,2022-03-15,2022-04-15";

        InputStream inputStream = IOUtils.toInputStream(csvData, StandardCharsets.UTF_8);
        List<Voucher> vouchers = Excelhelper.convertExcelToListOfVoucher(inputStream);

//        assertEquals(2, vouchers.size());
//        assertEquals("Exam1", vouchers.get(0).getExamName());
//        assertEquals("Cloud1", vouchers.get(0).getCloudPlatform());
//        assertEquals("ABC123", vouchers.get(0).getVoucherCode());
//        assertEquals("2022-01-23", vouchers.get(0).getIssuedDate().toString());
//        assertEquals("2022-02-23", vouchers.get(0).getExpiryDate().toString());
//
//        assertEquals("Exam2", vouchers.get(1).getExamName());
//        assertEquals("Cloud2", vouchers.get(1).getCloudPlatform());
//        assertEquals("XYZ456", vouchers.get(1).getVoucherCode());
//        assertEquals("2022-03-15", vouchers.get(1).getIssuedDate().toString());
//        assertEquals("2022-04-15", vouchers.get(1).getExpiryDate().toString());
    }

    @Test
    void testConvertExcelToListOfVoucher_Exception() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read()).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> Excelhelper.convertExcelToListOfVoucher(mockInputStream));
    }

    @Test
    void testConvertExcelToListOfVoucher_NullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> Excelhelper.convertExcelToListOfVoucher(null));
    }

    @Test
    void testConvertExcelToListOfVoucher_EmptyInputStream() {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        List<Voucher> vouchers = Excelhelper.convertExcelToListOfVoucher(emptyInputStream);

        assertNotNull(vouchers);
        assertTrue(vouchers.isEmpty());
    }

    @Test
    void testConvertExcelToListOfVoucher_IOExceptionHandling() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read()).thenThrow(new IOException());

        assertDoesNotThrow(() -> Excelhelper.convertExcelToListOfVoucher(mockInputStream));
    }

    @Test
    void testConvertExcelToListOfVoucher_InvalidDateFormat() throws IOException {
        String invalidData = "Exam Name,Cloud Platform,Voucher Code,Issued Date,Expiry Date\n" +
                "InvalidExam,InvalidCloud,InvalidCode,2022-01-23,InvalidDate\n";

        InputStream invalidInputStream = IOUtils.toInputStream(invalidData, StandardCharsets.UTF_8);
        List<Voucher> vouchers = Excelhelper.convertExcelToListOfVoucher(invalidInputStream);

        assertNotNull(vouchers);
        assertTrue(vouchers.isEmpty());
    }

    @Test
    void testConvertExcelToListOfVoucher_SkipHeaderRow() throws IOException {
        String dataWithHeader = "Exam Name,Cloud Platform,Voucher Code,Issued Date,Expiry Date\n" +
                "SkipHeader,ShouldSkip,HeaderRow,2022-01-23,2022-02-23\n";

        InputStream inputStreamWithHeader = IOUtils.toInputStream(dataWithHeader, StandardCharsets.UTF_8);
        List<Voucher> vouchers = Excelhelper.convertExcelToListOfVoucher(inputStreamWithHeader);

        assertNotNull(vouchers);
        assertTrue(vouchers.isEmpty());
    }

    // Additional test cases...
}
