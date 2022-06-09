package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.demo.modelo.Movimiento;
import com.example.demo.payload.RequestMovimiento;
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
	      if (cuentaId==null)
	          return new ResponseEntity<>( HttpStatus.NO_CONTENT);
	      else
	    	  movimientos= movimientoRepository.fecthByCuentaId(cuentaId);
	      if (movimientos==null) {
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
	public ResponseEntity<Boolean> createMovimiento(@RequestBody RequestMovimiento requestMovimiento) {
		try {
			return new ResponseEntity<>(movimientoService.crearMovimiento(requestMovimiento), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/movimiento/{id}")
	public ResponseEntity<Movimiento> updateMovimiento(@PathVariable("id") long id, @RequestBody RequestMovimiento requestMovimiento) {
		try {
			Optional<Movimiento> movimientoSelected = movimientoRepository.findById(id);
			if (movimientoSelected.isPresent()) {
				System.out.println(movimientoSelected.get());
				Movimiento movimiento = movimientoSelected.get();
				movimiento.setSaldo(requestMovimiento.getSaldo());
				movimiento.setTipo(requestMovimiento.getTipo());
				movimiento.setValor(requestMovimiento.getValor());
				movimiento.setFecha(requestMovimiento.getFecha());
				movimiento=movimientoRepository.save(movimiento);
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

}