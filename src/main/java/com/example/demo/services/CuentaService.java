package com.example.demo.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.loader.plan.exec.process.internal.AbstractRowReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.RequestClientDto;
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
	public Cuenta crearCuenta(RequestClientDto requestClient) {
		Cuenta cuenta = null;
		try {
			Optional<Cliente> cliente = clienteRepository.findById(requestClient.getClienteId());
			if (cliente.isPresent()) {
				cuenta = Cuenta.builder().estado(requestClient.getCuenta().getEstado()).cliente(cliente.get())
						.saldoInicial(requestClient.getCuenta().getSaldoInicial())
						.numeroCuenta(requestClient.getCuenta().getNumeroCuenta())
						.tipoCuenta(requestClient.getCuenta().getTipoCuenta()).build();
				cuenta=cuentaRepository.save(cuenta);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		return cuenta;
	}

}
