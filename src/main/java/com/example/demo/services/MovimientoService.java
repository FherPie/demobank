package com.example.demo.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.RequestMovimientoDto;
import com.example.demo.modelo.Cuenta;
import com.example.demo.modelo.ETipoMovimiento;
import com.example.demo.modelo.Movimiento;
import com.example.demo.repository.CuentaRepository;
import com.example.demo.repository.MovimientoRepository;
import com.example.demo.util.Constante;

@Service
public class MovimientoService {

	@Autowired
	CuentaRepository cuentaRepository;

	@Autowired
	MovimientoRepository movimientoRepository;

	@Transactional
	public String crearMovimiento(RequestMovimientoDto requestMovimiento) {
		try {
			Optional<Cuenta> cuenta = cuentaRepository.findById(requestMovimiento.getCuentaId());

			List<Movimiento> movimientos = movimientoRepository.fecthByCuentaId(requestMovimiento.getCuentaId());
		    return  validaryCrearMovimiento(movimientos, cuenta.get(), requestMovimiento);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}

	}

	private String validaryCrearMovimiento(List<Movimiento> movimientos, Cuenta cuenta,
			RequestMovimientoDto requestMovimiento) {
		try {
			Movimiento movimiento = null;
			
//			requestMovimiento.getValor().compareTo(BigDecimal.valueOf(Constante.MONTO_REIIRO_MAXIMO))>0
			if (movimientos.isEmpty()) {// Si no hay movimientos
				if (requestMovimiento.getTipo() == ETipoMovimiento.RETIRO) {
					if (requestMovimiento.getValor().compareTo(cuenta.getSaldoInicial()) > 0) { // Mayor retiro que
																								// saldo incial
						return Constante.SALDO_NO_DISPONIBLE;
					} else {
						movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
								.saldo(cuenta.getSaldoInicial().subtract(requestMovimiento.getValor()))
								.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
						movimientoRepository.save(movimiento);
						return Constante.TRANSACCION_EXITOSA;
					}
				} else { // DEPOSITO
					movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
							.saldo(requestMovimiento.getValor().add(cuenta.getSaldoInicial()))
							.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
					movimientoRepository.save(movimiento);
					return Constante.TRANSACCION_EXITOSA;
				}
			} else { // Movimientos
				Movimiento ultimoMovimiento = movimientos.get(movimientos.size() - 1);
				if (requestMovimiento.getTipo() == ETipoMovimiento.RETIRO) {
					if (requestMovimiento.getValor().compareTo(ultimoMovimiento.getSaldo()) > 0) { // Mayor retiro que
																								// saldO
						return Constante.SALDO_NO_DISPONIBLE;
					} else {
						movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
								.saldo(ultimoMovimiento.getSaldo().subtract(requestMovimiento.getValor()))
								.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
						movimientoRepository.save(movimiento);
						return Constante.TRANSACCION_EXITOSA;
					}
				} else { // DEPOSITO
					movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
							.saldo(requestMovimiento.getValor().add(ultimoMovimiento.getSaldo()))
							.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
					movimientoRepository.save(movimiento);
					return Constante.TRANSACCION_EXITOSA;
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	

	

	public void guardarMovimiento(Cuenta cuenta, RequestMovimientoDto requestMovimiento) {
		Movimiento movimiento=null;
		movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
				.saldo(cuenta.getSaldoInicial().subtract(requestMovimiento.getValor()))
				.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
		movimientoRepository.save(movimiento);
	}

	public void guardarMovimiento2(Cuenta cuenta, RequestMovimientoDto requestMovimiento, Movimiento ultimoMovimiento) {
		Movimiento movimiento=null;
		movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
				.saldo(requestMovimiento.getValor().add(ultimoMovimiento.getSaldo()))
				.tipo(requestMovimiento.getTipo()).valor(requestMovimiento.getValor()).build();
		movimientoRepository.save(movimiento);
	}

}
