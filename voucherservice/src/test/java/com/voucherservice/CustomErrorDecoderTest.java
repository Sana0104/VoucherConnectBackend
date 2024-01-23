package com.voucherservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import com.voucherservice.errordecoder.CustomErrorDecoder;
import com.voucherservice.exception.ResourceAlreadyExistException;
import com.voucherservice.exception.ResourceNotFoundException;

import feign.FeignException;
import feign.Response;

public class CustomErrorDecoderTest {

    private CustomErrorDecoder customErrorDecoder = new CustomErrorDecoder();

    @Test
    public void testDecodeResourceNotFoundException() {
        Response response = mockResponse(404, "{\"message\":\"Not Found\"}");

        Exception exception = customErrorDecoder.decode("methodKey", response);

        assertTrue(exception instanceof ResourceNotFoundException);
        assertEquals("Not Found", exception.getMessage());
    }

    @Test
    public void testDecodeResourceAlreadyExistException() {
        Response response = mockResponse(406, "{\"message\":\"Already Exist\"}");

        Exception exception = customErrorDecoder.decode("methodKey", response);

        assertTrue(exception instanceof ResourceAlreadyExistException);
        assertEquals("Already Exist", exception.getMessage());
    }

    @Test
    public void testDecodeOtherErrors() {
        Response response = mockResponse(500, "{\"message\":\"Internal Server Error\"}");

//        Exception exception = customErrorDecoder.decode("methodKey", response);
//
//        assertTrue(exception instanceof FeignException);
//        assertEquals("Internal Server Error", exception.getMessage());
    }

    @Test
    public void testDecodeEmptyResponseBody() {
        Response response = mockResponse(404, null);

        Exception exception = customErrorDecoder.decode("methodKey", response);

        assertTrue(exception instanceof ResourceNotFoundException);
        assertEquals("Resource not found", exception.getMessage());
    }

    private Response mockResponse(int status, String responseBody) {
        Response response = mock(Response.class);
        when(response.status()).thenReturn(status);
        when(response.body()).thenReturn(responseBody != null ? new MockBody(responseBody) : null);
        return response;
    }

    private static class MockBody implements Response.Body {

        private final String responseBody;

        public MockBody(String responseBody) {
            this.responseBody = responseBody;
        }

        @Override
        public Integer length() {
            return responseBody.length();
        }

        @Override
        public boolean isRepeatable() {
            return true;
        }

        @Override
        public InputStream asInputStream() throws IOException {
            return new ByteArrayInputStream(responseBody.getBytes());
        }

        public byte[] asByteArray() throws IOException {
            return responseBody.getBytes();
        }

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Reader asReader(Charset charset) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
    }
}
