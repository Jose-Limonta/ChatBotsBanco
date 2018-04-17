package com.bots.bots;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.clivern.racter.BotPlatform;

import junit.framework.TestCase;

public class InicioControllerTest extends TestCase {

	private static final Log LOGGER = LogFactory.getLog(InicioControllerTest.class);

	@Test
	public void testUploadConfigurationFile() throws IOException {
		BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
		LOGGER.info(platform.getName());
		assertEquals(platform.getName(), "Racter");
	}
	
	@Autowired
	private MockMvc mockServicio;
	
	@Test
	public void testDoGetAction() throws Exception{
		MultiValueMap<String, String> parametros = new LinkedMultiValueMap<>();
		parametros.add("hub.mode", "subscribe");
		parametros.add("hub.verify_token", "EAADDERZBg");
		parametros.add("hub.challenge", "401911800");
		MockHttpServletRequestBuilder reqMock = MockMvcRequestBuilders
				.get("/webhook").params(parametros);
		MvcResult resultadoMock = mockServicio.perform(reqMock)
				.andReturn();
		MockHttpServletResponse responseServicioMock = resultadoMock.getResponse();
		
		if( HttpStatus.OK.value() == responseServicioMock.getStatus() ){
			String jsonInString = responseServicioMock.getContentAsString();
			assertEquals("401911800", jsonInString);
		}		
	}
	
	public final String JSONPOST_SETTEXT = "{"
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
							+ "\"text\":\"hola\""
						+ "}"
					+ "}]"
				+ "}]"
			+ "}";
	
	@Test
	public void testPostAction() throws Exception {
		MockHttpServletRequestBuilder reqMock = MockMvcRequestBuilders
				.post("/webhook")
				.accept(MediaType.TEXT_PLAIN)
				.content(JSONPOST_SETTEXT)
				.contentType(MediaType.TEXT_PLAIN);
		
		MvcResult resultadoMock = mockServicio.perform(reqMock).andReturn();
		
		MockHttpServletResponse responseServicioMock = resultadoMock.getResponse();
		
		if( HttpStatus.OK.value() == responseServicioMock.getStatus() ){
			String jsonInString = responseServicioMock.getContentAsString();
			assertEquals("ok", jsonInString);
		}
	}
}