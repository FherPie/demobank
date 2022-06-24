package com.example.demo.integracion;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.example.demo.controller.MovimientoController;
import com.example.demo.dtos.ClientDto;
import com.example.demo.dtos.RequestCuentaDto;
import com.example.demo.dtos.MovimientoDto;
import com.example.demo.modelo.Cuenta;
import com.example.demo.modelo.EEstado;
import com.example.demo.modelo.ETipoCuenta;
import com.example.demo.modelo.ETipoMovimiento;
import com.example.demo.modelo.Movimiento;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MovimientoControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private MovimientoController controller;

	//@Test
	public void movcontrollerLoads() throws Exception {
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


	////@Test
	public void testDepositoMovimientoSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/movimiento/";
		URI uri = new URI(baseUrl);
		
		MovimientoDto requestMovimiento = MovimientoDto.builder().cuentaId(4L).saldo(new BigDecimal(2000))
				.tipo(ETipoMovimiento.DEPOSITO).valor(new BigDecimal(5000)).build();
		
		//RequestClient requestClient = RequestClient.builder().cuenta(requestCuenta).clienteId(2L).build();
		HttpEntity<MovimientoDto> request = new HttpEntity<>(requestMovimiento);
		System.out.println(baseUrl);
		ResponseEntity<Boolean> result = this.restTemplate.postForEntity(uri, request, Boolean.class);
		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(true, result.getBody());
	}
	
	
	////@Test
	public void testDepositoMovimientoUnSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/movimiento/";
		URI uri = new URI(baseUrl);
		
		MovimientoDto requestMovimiento = MovimientoDto.builder().cuentaId(4L).saldo(new BigDecimal(2000))
				.tipo(ETipoMovimiento.RETIRO).valor(new BigDecimal(50000)).build();
		
		//RequestClient requestClient = RequestClient.builder().cuenta(requestCuenta).clienteId(2L).build();
		HttpEntity<MovimientoDto> request = new HttpEntity<>(requestMovimiento);
		System.out.println(baseUrl);
		ResponseEntity<Boolean> result = this.restTemplate.postForEntity(uri, request, Boolean.class);
		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(false, result.getBody());
	}
	
	
	////@Test
	public void testRetiroMovimientoSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/movimiento/";
		URI uri = new URI(baseUrl);
		
		MovimientoDto requestMovimiento = MovimientoDto.builder().cuentaId(4L).saldo(new BigDecimal(2000))
				.tipo(ETipoMovimiento.RETIRO).valor(new BigDecimal(12000)).build();
		
		//RequestClient requestClient = RequestClient.builder().cuenta(requestCuenta).clienteId(2L).build();
		HttpEntity<MovimientoDto> request = new HttpEntity<>(requestMovimiento);
		System.out.println(baseUrl);
		ResponseEntity<Boolean> result = this.restTemplate.postForEntity(uri, request, Boolean.class);
		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(true, result.getBody());
	}
	
	//@Test
	public void testRetiroMovimientoSuccess2() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/movimiento/";
		URI uri = new URI(baseUrl);
		
		MovimientoDto requestMovimiento = MovimientoDto.builder().cuentaId(4L).saldo(new BigDecimal(2000))
				.tipo(ETipoMovimiento.RETIRO).valor(new BigDecimal(1000)).build();
		
		//RequestClient requestClient = RequestClient.builder().cuenta(requestCuenta).clienteId(2L).build();
		HttpEntity<MovimientoDto> request = new HttpEntity<>(requestMovimiento);
		System.out.println(baseUrl);
		ResponseEntity<Boolean> result = this.restTemplate.postForEntity(uri, request, Boolean.class);
		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(true, result.getBody());
	}
	
	
	////@Test
	public void testListMovimientoSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "";
		// URI uri = new URI(baseUrl);
		System.out.println(baseUrl);
		URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/movimiento/").queryParam("cuentaId", 4L).build()
				.toUri();
		ResponseEntity<List> result = this.restTemplate.getForEntity(uri, List.class);
		// Verify request succeed
		System.out.println(result.getBody());
		Assert.assertEquals(200, result.getStatusCodeValue());
	}
	
	////@Test
	public void deleteMovimientoSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/movimiento/{id}";
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("id", "3");
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
		ResponseEntity<HttpStatus> result = this.restTemplate.exchange(builder.buildAndExpand(pathParams).toUri(),HttpMethod.DELETE,null,HttpStatus.class);
		// Verify request succeed
		Assert.assertEquals(204, result.getStatusCodeValue());
	}
	
	////@Test
	public void updateMovimientoSuccess() throws URISyntaxException {
		final String baseUrl = "http://localhost:" + randomServerPort + "/api/movimiento/{id}";
		// URI uri = new URI(baseUrl);
		System.out.println(baseUrl);
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "19");
		
		MovimientoDto requestMovimiento= MovimientoDto.builder()
				.saldo(new BigDecimal(3000))
				.tipo(ETipoMovimiento.RETIRO)
				.fecha(new Date())
				.valor(new BigDecimal(10000))
				.build();
	
		HttpEntity<MovimientoDto> request = new HttpEntity<>(requestMovimiento);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
	
		ResponseEntity<Movimiento> result = this.restTemplate.exchange(builder.buildAndExpand(params).toUri(), HttpMethod.PUT,request , Movimiento.class);
		// Verify request succeed
		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(new BigDecimal(3000), result.getBody().getSaldo());
	}

}
