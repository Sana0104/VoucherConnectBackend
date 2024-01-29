package com.va.voucher_request.exceptions;

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

import com.va.voucher_request.errordecoder.CustomErrorDecoder;

import feign.Response;

 class CustomErrorDecoderTest {

    private CustomErrorDecoder customErrorDecoder = new CustomErrorDecoder();

  

    @Test
     void testDecodeEmptyResponseBody() {
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
