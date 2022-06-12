package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
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

import com.example.demo.dtos.RequestClientDto;
import com.example.demo.dtos.RequestCuentaDto;
import com.example.demo.modelo.Cuenta;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.CuentaRepository;
import com.example.demo.services.ClientService;
import com.example.demo.services.CuentaService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CuentaController {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	ClientService clienteService;
	

	@Autowired
	CuentaService cuentaService;
	
	@Autowired
	CuentaRepository cuentaRepository;
	
	@GetMapping("/cuenta")
	  public ResponseEntity<List<RequestCuentaDto>> getAllCuentaByClienteIdentification(@RequestParam(required = false) Long cliendId) {	
		System.out.println(cliendId);
	    try {
	    	List<Cuenta> listCuenta = null;
	      if (cliendId==null)
	    	  listCuenta= cuentaRepository.fecthCuentas();
	      else
	    	  listCuenta= cuentaRepository.fecthByCliente(cliendId);
	      
	   List<RequestCuentaDto> listaRequestCuentaDto=   listCuenta.stream().map(cuenta-> {
	    	  RequestCuentaDto requestCuenta= RequestCuentaDto.builder()
	    			  .numeroCuenta(cuenta.getNumeroCuenta())
	    			  .tipoCuenta(cuenta.getTipoCuenta())
	    			  .saldoInicial(cuenta.getSaldoInicial())
	    			  .estado(cuenta.getEstado())
	    			  .cliente(cuenta.getCliente().getPersona().getNombre()+" "+cuenta.getCliente().getPersona().getApellido())
	    			  .build();
	    	  return requestCuenta;
	      }).collect(Collectors.toList());
	            
	      if (listaRequestCuentaDto==null) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(listaRequestCuentaDto, HttpStatus.OK);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	@GetMapping("/cuenta/{id}")
	public ResponseEntity<Cuenta> getCuentaById(@PathVariable("id") long id) {
		Optional<Cuenta> cuentaData = cuentaRepository.findById(id);
		if (cuentaData.isPresent()) {
			return new ResponseEntity<>(cuentaData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/cuenta")
	public ResponseEntity<Cuenta> createCuenta(@RequestBody RequestClientDto requestClient) {
		try {
			return new ResponseEntity<>(cuentaService.crearCuenta(requestClient), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("static-access")
	@PutMapping("/cuenta/{id}")
	public ResponseEntity<Cuenta> updateCuenta(@PathVariable("id") long id, @RequestBody RequestCuentaDto requestCuenta) {
		try {
			Optional<Cuenta> cuentaSelected = cuentaRepository.findById(id);
			if (cuentaSelected.isPresent()) {
				System.out.println(cuentaSelected.get());
				Cuenta cuenta = cuentaSelected.get();
				cuenta.setEstado(requestCuenta.getEstado());
				cuenta.setSaldoInicial(requestCuenta.getSaldoInicial());
				cuenta.setNumeroCuenta(requestCuenta.getNumeroCuenta());
				cuenta.setTipoCuenta(requestCuenta.getTipoCuenta());
				cuenta=cuentaRepository.save(cuenta);
				return new ResponseEntity<>(cuenta, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/cuenta/{id}")
	public ResponseEntity<HttpStatus> deleteCuenta(@PathVariable("id") long id) {
		try {
			cuentaRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}