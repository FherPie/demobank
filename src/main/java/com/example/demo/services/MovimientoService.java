package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modelo.Cuenta;
import com.example.demo.modelo.ETipoMovimiento;
import com.example.demo.modelo.Movimiento;
import com.example.demo.payload.RequestMovimiento;
import com.example.demo.repository.CuentaRepository;
import com.example.demo.repository.MovimientoRepository;

@Service
public class MovimientoService {

	@Autowired
	CuentaRepository cuentaRepository;

	@Autowired
	MovimientoRepository movimientoRepository;

	@Transactional
	public boolean crearMovimiento(RequestMovimiento requestMovimiento) {
		try {
			Optional<Cuenta> cuenta = cuentaRepository.findById(requestMovimiento.getCuentaId());

			List<Movimiento> movimientos = movimientoRepository.fecthByCuentaId(requestMovimiento.getCuentaId());

			if (cuenta.isPresent() && validaryCrearMovimiento(movimientos, cuenta.get(), requestMovimiento)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}

	}

	private boolean validaryCrearMovimiento(List<Movimiento> movimientos, Cuenta cuenta,
			RequestMovimiento requestMovimiento) {

		try {
			Movimiento movimiento = null;
			if (movimientos.isEmpty()) {// Si no hay movimientos
				if (requestMovimiento.getTipo() == ETipoMovimiento.RETIRO) {
					if (requestMovimiento.getValor().compareTo(cuenta.getSaldoInicial()) > 0) { // Mayor retiro que
																								// saldo incial
						return false;
					} else {
						movimiento = Movimiento.builder().cuenta(cuenta).fecha(LocalDateTime.now())
								.saldo(cuenta.getSaldoInicial().subtract(requestMovimiento.getValor()))
								.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
						movimientoRepository.save(movimiento);
					}
				} else { // DEPOSITO
					movimiento = Movimiento.builder().cuenta(cuenta).fecha(LocalDateTime.now())
							.saldo(requestMovimiento.getValor().add(cuenta.getSaldoInicial()))
							.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
					movimientoRepository.save(movimiento);
				}
			} else { // Movimientos
				Movimiento ultimoMovimiento = movimientos.get(movimientos.size() - 1);
				if (requestMovimiento.getTipo() == ETipoMovimiento.RETIRO) {
					if (requestMovimiento.getValor().compareTo(ultimoMovimiento.getSaldo()) > 0) { // Mayor retiro que
																								// saldO
						return false;
					} else {
						movimiento = Movimiento.builder().cuenta(cuenta).fecha(LocalDateTime.now())
								.saldo(ultimoMovimiento.getSaldo().subtract(requestMovimiento.getValor()))
								.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
						movimientoRepository.save(movimiento);
					}
				} else { // DEPOSITO
					movimiento = Movimiento.builder().cuenta(cuenta).fecha(LocalDateTime.now())
							.saldo(requestMovimiento.getValor().add(ultimoMovimiento.getSaldo()))
							.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
					movimientoRepository.save(movimiento);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

}
