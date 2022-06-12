package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ReporteCuentasDto;
import com.example.demo.dtos.RequestMovimientoDto;
import com.example.demo.modelo.ETipoMovimiento;
import com.example.demo.modelo.Movimiento;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.MovimientoRepository;
import com.example.demo.services.ClientService;
import com.example.demo.services.CuentaService;
import com.example.demo.services.MovimientoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MovimientoController {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	ClientService clienteService;

	@Autowired
	CuentaService cuentaService;

	@Autowired
	MovimientoService movimientoService;

	@Autowired
	MovimientoRepository movimientoRepository;

	@GetMapping("/movimiento")
	public ResponseEntity<List<Movimiento>> getAllMovimientoByCuentaId(@RequestParam(required = false) Long cuentaId) {
		System.out.println(cuentaId);
		try {
			List<Movimiento> movimientos = null;
			if (cuentaId == null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				movimientos = movimientoRepository.fecthByCuentaId(cuentaId);
			if (movimientos == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(movimientos, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/movimiento/{id}")
	public ResponseEntity<Movimiento> getMovimientoById(@PathVariable("id") long id) {
		Optional<Movimiento> movimientoData = movimientoRepository.findById(id);
		if (movimientoData.isPresent()) {
			return new ResponseEntity<>(movimientoData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/movimiento")
	public ResponseEntity<String> createMovimiento(@RequestBody RequestMovimientoDto requestMovimiento) {
		System.out.println(requestMovimiento.getFecha());
		System.out.println(Locale.getDefault());
		System.out.println(TimeZone.getDefault());
		
		try {
			return new ResponseEntity<>(movimientoService.crearMovimiento(requestMovimiento), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/movimiento/{id}")
	public ResponseEntity<Movimiento> updateMovimiento(@PathVariable("id") long id,
			@RequestBody RequestMovimientoDto requestMovimiento) {
		try {
			Optional<Movimiento> movimientoSelected = movimientoRepository.findById(id);
			if (movimientoSelected.isPresent()) {
				System.out.println(movimientoSelected.get());
				Movimiento movimiento = movimientoSelected.get();
				movimiento.setSaldo(requestMovimiento.getSaldo());
				movimiento.setTipo(requestMovimiento.getTipo());
				movimiento.setValor(requestMovimiento.getValor());
				movimiento.setFecha(requestMovimiento.getFecha());
				movimiento = movimientoRepository.save(movimiento);
				return new ResponseEntity<>(movimiento, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/movimiento/{id}")
	public ResponseEntity<HttpStatus> deleteMovimiento(@PathVariable("id") long id) {
		try {
			movimientoRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/reporteMovimientoxCliente")
	public ResponseEntity<List<ReporteCuentasDto>> getAllMovimientoByClienteId(@RequestParam(required = true) Long clienteId, @RequestParam(required = true) Date startDate, @RequestParam(required = true) Date  endDate  ) {
		System.out.println(clienteId);
		System.out.println(startDate);
		System.out.println(endDate);
		
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");

		try {
			List<Movimiento>  movimientos = null;
			if (clienteId == null)
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				movimientos = movimientoRepository.fecthMovimientoBetweenDatesAndClientID(startDate, endDate, clienteId);
			   
			 List<ReporteCuentasDto> reportes= movimientos.stream().map(movimiento -> {
				 ReporteCuentasDto reporte;
				reporte = ReporteCuentasDto.builder()
						 .nombreCliente(movimiento.getCuenta().getCliente().getPersona().getNombre()+ " "+movimiento.getCuenta().getCliente().getPersona().getApellido())
						 .numeroCuenta(movimiento.getCuenta().getNumeroCuenta())
						 .saldoDisponible(String.format("%.2f", movimiento.getSaldo()))
						 .movimiento(String.format("%.2f", movimiento.getValor()))
						 .tipoCuenta(movimiento.getCuenta().getTipoCuenta().toString())
						 .fecha( newFormat.format(movimiento.getFecha()))
						 .estado(movimiento.getCuenta().getEstado().toString())
						 .saldoInicial(String.format("%.2f",movimiento.getCuenta().getSaldoInicial()))
						 .build();
				  return reporte;
			 }).collect(Collectors.toList());;
			
			if (reportes == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(reportes	, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}