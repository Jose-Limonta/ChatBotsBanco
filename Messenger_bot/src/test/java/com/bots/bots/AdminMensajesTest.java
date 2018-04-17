package com.bots.bots;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminMensajesTest extends TestCase {

	private static final Log LOGGER = LogFactory.getLog(AdminMensajesTest.class);
	
	@Autowired
	private MockMvc mockServicio;
	
	public final String JSONPOST_SOURCETEXT = "{"
			+ "\"object\":\"page\","
			+ "\"entry\":[{"
				+ "\"id\":\"196856110912106\","
				+ "\"time\":"+new Date().getTime()+","
				+ "\"messaging\":[{"
					+ "\"sender\":{"
						+ "\"id\":\"1809264795810706\""
					+ "},"
					+ "\"recipient\":{"
						+ "\"id\":\"196856110912106\""
					+ "},"
					+ "\"timestamp\":"+new Date().getTime()+","
					+ "\"message\":{"
						+ "\"mid\":\"mid.$cAADp_rPrQEZo4q_SYVisWqNSrEj3\","
						+ "\"seq\":17120,"
						+ "\"text\":\"472389473289 432\""
					+ "}"
				+ "}]"
			+ "}]"
		+ "}";
	
	public final String JSON_QUICK_REPLY_PAYLOAD = "{"
			+ "\"object\":\"page\","
			+ "\"entry\":[{"
				+ "\"id\":\"196856110912106\","
				+ "\"time\":"+new Date().getTime()+","
				+ "\"messaging\":[{"
					+ "\"sender\":{"
						+ "\"id\":\"1809264795810706\""
					+ "},"
					+ "\"recipient\":{"
						+ "\"id\":\"196856110912106\""
					+ "},"
					+ "\"timestamp\":"+new Date().getTime()+","
					+ "\"message\":{"
						+ "\"quick_reply\":{"
							+ "\"payload\":\"consulta_saldo_click\""
						+ "},"
						+ "\"mid\":\"mid.$cAADp_rPrQEZo5qpFx1itWUBYB8I1\","
						+ "\"seq\":17168,"
						+ "\"text\":\"Consulta saldo\""
					+ "}"
				+ "}]"
			+ "}]"
		+ "}";
		
	@Test
	public void testSelectionQuickReply() throws Exception {
		MockHttpServletRequestBuilder reqMock = MockMvcRequestBuilders
				.post("/webhook")
				.accept(MediaType.TEXT_PLAIN)
				.content(JSON_QUICK_REPLY_PAYLOAD)
				.contentType(MediaType.TEXT_PLAIN);
		
		MvcResult resultadoMock = mockServicio.perform(reqMock).andReturn();
		
		MockHttpServletResponse responseServicioMock = resultadoMock.getResponse();
		
		if( HttpStatus.OK.value() == responseServicioMock.getStatus() ){
			String jsonInString = responseServicioMock.getContentAsString();
			assertEquals("ok", jsonInString);
		}
	}
	
	@Test
	public void testPostActionSetTransaccion() throws Exception {
		MockHttpServletRequestBuilder reqMock = MockMvcRequestBuilders
				.post("/webhook")
				.accept(MediaType.TEXT_PLAIN)
				.content(JSONPOST_SOURCETEXT)
				.contentType(MediaType.TEXT_PLAIN);
		
		MvcResult resultadoMock = mockServicio.perform(reqMock).andReturn();
		
		MockHttpServletResponse responseServicioMock = resultadoMock.getResponse();
		
		if( HttpStatus.OK.value() == responseServicioMock.getStatus() ){
			String jsonInString = responseServicioMock.getContentAsString();
			assertEquals("ok", jsonInString);
		}
	}
}	