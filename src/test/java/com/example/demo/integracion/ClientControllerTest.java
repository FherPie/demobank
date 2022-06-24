package com.example.demo.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.example.demo.controller.ClienteController;
import com.example.demo.dtos.ClientDto;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.EEstado;
import com.example.demo.modelo.EGenero;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		ClientDto requestClient = ClientDto.builder().email("clientes@hotmail.com")
				.apellido("Andrade")
				.nombre("Andres")
				.direccion("Monjas")
				.estado(EEstado.ACTIVO)
				.identificacion("17878764644")
				.genero(EGenero.MASCULINO)
				.telefono("098989898")
				.password("7777")
				.build();
		HttpEntity<ClientDto> request = new HttpEntity<>(requestClient);
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
		ResponseEntity<List> result = this.restTemplate.getForEntity(uri, List.class);
		// Verify request succeed
		ObjectMapper mapper = new ObjectMapper();
		//List<Cliente> pojo = mapper.convertValue(result.getBody(), List.class);
		
		List<Cliente> pojos = mapper.convertValue(
				result.getBody(),
			    new TypeReference<List<Cliente>>() { });
		
		Iterator<Cliente> clientIterator= pojos.iterator(); 
		
		try {
			while (clientIterator.hasNext()) {
				System.out.println("HOLA");
				Cliente cliente = clientIterator.next();
				Long id=	cliente.getId();
				final String baseUrl2= baseUrl + "/api/clients/";
				Map<String, Long> pathParams = new HashMap<String, Long>();
				pathParams.put("id", id);
				UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl2);
				ResponseEntity<HttpStatus> result2 = this.restTemplate.exchange(builder.buildAndExpand(pathParams).toUri(),HttpMethod.DELETE,null,HttpStatus.class);
				// Verify request succeed
				Assert.assertEquals(204, result2.getStatusCodeValue());
			}		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
	}
	
	
	

}
