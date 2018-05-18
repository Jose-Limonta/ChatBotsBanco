package com.bots.bots;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.testing.util.TestableByteArrayInputStream;
import com.google.api.client.util.Key;

import junit.framework.TestCase;

public class ResponseTest extends TestCase {

	@Test
	public void testParseAsString_none() throws Exception {
		HttpTransport transport = new MockHttpTransport();
		HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
		HttpResponse response = request.execute();
		assertEquals("", response.parseAsString());
	}

	private static final String SAMPLE = "123\u05D9\u05e0\u05D9\u05D1";
	private static final String SAMPLE2 = "123abc";

	@Test
	public void testParseAsString_noContentType() throws Exception {
		HttpTransport transport = new MockHttpTransport() {
			@Override
			public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
				return new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
						result.setContent(SAMPLE2);
						return result;
					}
				};
			}
		};
		HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
		HttpResponse response = request.execute();
		assertEquals(SAMPLE2, response.parseAsString());
	}

	public void subtestStatusCode_negative(boolean throwExceptionOnExecuteError) throws Exception {
		HttpTransport transport = new MockHttpTransport() {
			@Override
			public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
				return new MockLowLevelHttpRequest().setResponse(new MockLowLevelHttpResponse().setStatusCode(-1));
			}
		};
		HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
		request.setThrowExceptionOnExecuteError(throwExceptionOnExecuteError);
		try {
			HttpResponse response = request.execute();
			assertEquals(0, response.getStatusCode());
			assertFalse(throwExceptionOnExecuteError);
		} catch (HttpResponseException e) {
			assertTrue(throwExceptionOnExecuteError);
			assertEquals(0, e.getStatusCode());
		}
	}

	public static class MyHeaders extends HttpHeaders {

		@Key
		public String foo;

		@Key
		public Object obj;

		@Key
		String[] r;
	}

	static final String ETAG_VALUE = "\"abc\"";

	@Test
	public void testHeaderParsing() throws Exception {
		HttpTransport transport = new MockHttpTransport() {
			@Override
			public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
				return new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
						result.addHeader("accept", "value");
						result.addHeader("foo", "bar");
						result.addHeader("goo", "car");
						result.addHeader("hoo", "dar");
						result.addHeader("hoo", "far");
						result.addHeader("obj", "o");
						result.addHeader("r", "a1");
						result.addHeader("r", "a2");
						result.addHeader("ETAG", ETAG_VALUE);
						return result;
					}
				};
			}
		};
		HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
		request.setResponseHeaders(new MyHeaders());
		HttpResponse response = request.execute();
		assertEquals("value", response.getHeaders().getAccept());
		assertEquals("bar", ((MyHeaders) response.getHeaders()).foo);
		assertEquals(Arrays.asList("o"), ((MyHeaders) response.getHeaders()).obj);
		assertEquals(Arrays.asList("a1", "a2"), Arrays.asList(((MyHeaders) response.getHeaders()).r));
		assertEquals(Arrays.asList("car"), response.getHeaders().get("goo"));
		assertEquals(Arrays.asList("dar", "far"), response.getHeaders().get("hoo"));
		assertEquals(ETAG_VALUE, response.getHeaders().getETag());
	}

	@Test
	public void testParseAs_noParser() throws Exception {
		try {
			new MockHttpTransport().createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL).execute()
					.parseAs(Object.class);
			fail("expected " + NullPointerException.class);
		} catch (NullPointerException e) {
			// expected
		}
	}

	@Test
	public void testParseAs_classNoContent() throws Exception {
		parseAsNoContent(true);
	}

	@Test
	public void testParseAs_typeNoContent() throws Exception {
		parseAsNoContent(false);
	}
	
	private void parseAsNoContent(boolean type) throws IOException {
		final MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();

		for (final int status : new int[] { HttpStatusCodes.STATUS_CODE_NO_CONTENT,
				HttpStatusCodes.STATUS_CODE_NOT_MODIFIED, 102 }) {
			HttpTransport transport = new MockHttpTransport() {
				@Override
				public LowLevelHttpRequest buildRequest(String method, final String url) throws IOException {
					return new MockLowLevelHttpRequest() {
						@Override
						public LowLevelHttpResponse execute() throws IOException {
							result.setStatusCode(status);
							result.setContentType(null);
							result.setContent(new ByteArrayInputStream(new byte[0]));
							return result;
						}
					};
				}
			};

			if( type ) {
				Object parsed = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL)
					.setThrowExceptionOnExecuteError(false).execute().parseAs(Object.class);
				assertNull(parsed);
			}else {
				Object parsed = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL)
						.setThrowExceptionOnExecuteError(false).execute().parseAs((Type) Object.class);
				assertNull(parsed);
			}
			
		}
	}

	public void testDownload() throws Exception {
		HttpTransport transport = new MockHttpTransport() {
			@Override
			public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
				return new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
						result.setContentType(Json.MEDIA_TYPE);
						result.setContent(SAMPLE);
						return result;
					}
				};
			}
		};
		HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
		HttpResponse response = request.execute();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		response.download(outputStream);
		assertEquals(SAMPLE, outputStream.toString("UTF-8"));
	}

	public void testDisconnectWithContent() throws Exception {
		final MockLowLevelHttpResponse lowLevelHttpResponse = new MockLowLevelHttpResponse();

		HttpTransport transport = new MockHttpTransport() {
			@Override
			public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
				return new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						lowLevelHttpResponse.setContentType(Json.MEDIA_TYPE);
						lowLevelHttpResponse.setContent(SAMPLE);
						return lowLevelHttpResponse;
					}
				};
			}
		};
		HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
		HttpResponse response = request.execute();

		assertFalse(lowLevelHttpResponse.isDisconnected());
		TestableByteArrayInputStream content = (TestableByteArrayInputStream) lowLevelHttpResponse.getContent();
		assertFalse(content.isClosed());
		response.disconnect();
		assertTrue(lowLevelHttpResponse.isDisconnected());
		assertTrue(content.isClosed());
	}

	@Test
	public void testDisconnectWithNoContent() throws Exception {
		final MockLowLevelHttpResponse lowLevelHttpResponse = new MockLowLevelHttpResponse();

		HttpTransport transport = new MockHttpTransport() {
			@Override
			public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
				return new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						return lowLevelHttpResponse;
					}
				};
			}
		};
		HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
		HttpResponse response = request.execute();

		assertFalse(lowLevelHttpResponse.isDisconnected());
		response.disconnect();
		assertTrue(lowLevelHttpResponse.isDisconnected());
	}
	
	@Test
	public void testGetContent_gzipNoContent() throws IOException {
		HttpTransport transport = new MockHttpTransport() {
			@Override
			public LowLevelHttpRequest buildRequest(String method, final String url) throws IOException {
				return new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
						result.setContent("");
						result.setContentEncoding("gzip");
						result.setContentType("text/plain");
						return result;
					}
				};
			}
		};
		HttpRequest request = transport.createRequestFactory().buildHeadRequest(HttpTesting.SIMPLE_GENERIC_URL);
		request.execute().getContent();
	}

}
