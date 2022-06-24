package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dtos.ClientDto;
import com.example.demo.dtos.RequestCuentaDto;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Cuenta;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.CuentaRepository;
import com.example.demo.repository.PersonaRepository;

@Service
public class CuentaService {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	PersonaRepository personaRepository;

	@Autowired
	CuentaRepository cuentaRepository;

	@Transactional
	public Cuenta crearCuenta(RequestCuentaDto requestCuenta) {
		Cuenta cuenta = null;
		try {
			Optional<Cliente> cliente = clienteRepository.findById(requestCuenta.getClienteId());
			if (cliente.isPresent()) {
				cuenta = Cuenta.builder().estado(requestCuenta.getEstado()).cliente(cliente.get())
						.saldoInicial(requestCuenta.getSaldoInicial())
						.numeroCuenta(requestCuenta.getNumeroCuenta())
						.tipoCuenta(requestCuenta.getTipoCuenta()).build();
				cuenta=cuentaRepository.save(cuenta);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		return cuenta;
	}
	
	
	public RequestCuentaDto getCuentaById( long id) {
		Optional<Cuenta> cuentaData = cuentaRepository.findById(id);
		if (cuentaData.isPresent()) {
	    	  RequestCuentaDto requestCuenta= RequestCuentaDto.builder()
	    			  .numeroCuenta(cuentaData.get().getNumeroCuenta())
	    			  .tipoCuenta(cuentaData.get().getTipoCuenta())
	    			  .saldoInicial(cuentaData.get().getSaldoInicial())
	    			  .estado(cuentaData.get().getEstado())
	    			  .cliente(cuentaData.get().getCliente().getPersona().getNombre()+" "+cuentaData.get().getCliente().getPersona().getApellido()+ " "+cuentaData.get().getCliente().getPersona().getIdentificacion())
	    			  .cuentaId(String.valueOf(cuentaData.get().getCuentaId()))
	    			  .build();
			return requestCuenta;
		} else {
			return null;
		}
	}


}
