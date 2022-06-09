package com.example.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.EEstado;
import com.example.demo.modelo.EGenero;
import com.example.demo.payload.RequestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClientControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private ClienteController controller;


	@Test
	public void clientcontrollerLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
	
	@Test
	public void testAddClienteSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/clients/";
		URI uri = new URI(baseUrl);		
		RequestClient requestClient = RequestClient.builder().email("clientes@hotmail.com")
				.apellido("Andrade")
				.nombre("Andres")
				.direccion("Monjas")
				.estado(EEstado.ACTIVO)
				.identificacion("17878764644")
				.genero(EGenero.MASCULINO)
				.telefono("098989898")
				.password("7777")
				.build();
		HttpEntity<RequestClient> request = new HttpEntity<>(requestClient);
		System.out.println(baseUrl);
		ResponseEntity<Cliente> result = this.restTemplate.postForEntity(uri, request, Cliente.class);
		// Verify request succeed
		Assert.assertEquals(201, result.getStatusCodeValue());
	}
	
	
	

	@Test
	public void testDeleteClienteSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort;	
		System.out.println(baseUrl);
		URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/clients/").queryParam("identification", "17878764644").build()
				.toUri();
		ResponseEntity<Cliente> result = this.restTemplate.getForEntity(uri, Cliente.class);
		// Verify request succeed
		Long id= result.getBody().getId();
		final String baseUrl2= baseUrl + "/api/clients/";
		
		Map<String, Long> pathParams = new HashMap<String, Long>();
		pathParams.put("id", id);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl2);
		ResponseEntity<HttpStatus> result2 = this.restTemplate.exchange(builder.buildAndExpand(pathParams).toUri(),HttpMethod.DELETE,null,HttpStatus.class);
		// Verify request succeed
		Assert.assertEquals(204, result2.getStatusCodeValue());
		
		
		
	
		URI uri = new URI(baseUrl);		
		RequestClient requestClient = RequestClient.builder().email("clientes@hotmail.com")
				.apellido("Andrade")
				.nombre("Andres")
				.direccion("Monjas")
				.estado(EEstado.ACTIVO)
				.identificacion("17878764644")
				.genero(EGenero.MASCULINO)
				.telefono("098989898")
				.password("7777")
				.build();
		HttpEntity<RequestClient> request = new HttpEntity<>(requestClient);
		System.out.println(baseUrl);
		ResponseEntity<Cliente> result = this.restTemplate.postForEntity(uri, request, Cliente.class);
		// Verify request succeed
		Assert.assertEquals(201, result.getStatusCodeValue());
	}
	

}
