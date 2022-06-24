package com.example.demo.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
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
import com.example.demo.dtos.RequestCuentaDto;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Cuenta;
import com.example.demo.modelo.EEstado;
import com.example.demo.modelo.ETipoCuenta;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CuentaControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private ClienteController controller;

	//@Test
	public void clientcontrollerLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	////@Test
	public void testAddCuentaSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/cuenta/";
		URI uri = new URI(baseUrl);
		RequestCuentaDto requestCuenta = RequestCuentaDto.builder().estado(EEstado.ACTIVO).numeroCuenta("123456789")
				.saldoInicial(new BigDecimal(2000)).tipoCuenta(ETipoCuenta.AHORROS).build();
		ClientDto requestClient = ClientDto.builder().cuenta(requestCuenta).clienteId(2L).build();
		HttpEntity<ClientDto> request = new HttpEntity<>(requestClient);
		System.out.println(baseUrl);
		ResponseEntity<Cuenta> result = this.restTemplate.postForEntity(uri, request, Cuenta.class);
		// Verify request succeed
		Assert.assertEquals(201, result.getStatusCodeValue());
	}

	//@Test
	public void testListCuentaSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "";
		// URI uri = new URI(baseUrl);
		System.out.println(baseUrl);
		URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/cuenta/").queryParam("cliendId", 2L).build()
				.toUri();
		ResponseEntity<Cliente> result = this.restTemplate.getForEntity(uri, Cliente.class);
		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
	}

	////@Test
	public void updateCuentaSuccess() throws URISyntaxException {

		
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/cuenta/{id}";
		// URI uri = new URI(baseUrl);
		System.out.println(baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "4");
		
		RequestCuentaDto requestCuenta = RequestCuentaDto.builder().estado(EEstado.ACTIVO).numeroCuenta("123456789")
				.saldoInicial(new BigDecimal(3000)).tipoCuenta(ETipoCuenta.AHORROS).build();
		HttpEntity<RequestCuentaDto> request = new HttpEntity<>(requestCuenta);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
		//URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/cuenta/").path("/{id}").build().toUri();
		ResponseEntity<Cuenta> result = this.restTemplate.exchange(builder.buildAndExpand(params).toUri(), HttpMethod.PUT,request , Cuenta.class);
		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(new BigDecimal(3000), result.getBody().getSaldoInicial());
	}

	////@Test
	public void deleteCuentaSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/cuenta/{id}";
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("id", "3");
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
		ResponseEntity<HttpStatus> result = this.restTemplate.exchange(builder.buildAndExpand(pathParams).toUri(),HttpMethod.DELETE,null,HttpStatus.class);
		// Verify request succeed
		Assert.assertEquals(204, result.getStatusCodeValue());
	}

}
