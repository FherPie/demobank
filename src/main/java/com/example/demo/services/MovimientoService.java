package com.example.demo.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dtos.CuentaDto;
import com.example.demo.dtos.MensajeMovimientoDto;
import com.example.demo.dtos.MovimientoDto;
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
	public MensajeMovimientoDto crearMovimiento(MovimientoDto requestMovimiento) {
		System.out.println(requestMovimiento.getFecha());
		System.out.println(Locale.getDefault());
		System.out.println(TimeZone.getDefault());
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

	@Transactional
	private MensajeMovimientoDto validaryCrearMovimiento(List<Movimiento> movimientos, Cuenta cuenta,
			MovimientoDto requestMovimiento) {
		MensajeMovimientoDto mensaje= new MensajeMovimientoDto();
		try {
			Movimiento movimiento = null;
			BigDecimal valor=null;
			
			List<Movimiento> movimientosParaSaldo=movimientoRepository.fecthByMovimientoBetweenDatesAndCuentaId(requestMovimiento.getFecha(), requestMovimiento.getFecha(), cuenta.getCuentaId());	
			valor= movimientosParaSaldo.stream()
					.reduce(BigDecimal.ZERO, (valorparcial, movmientox) -> ((BigDecimal) valorparcial).add(movmientox.getValor()) , BigDecimal::add);
			
			//ystem.out.println(valor.abs());
			if(requestMovimiento.getValor().compareTo(BigDecimal.ZERO)<0 && valor.abs().add(requestMovimiento.getValor().abs()).compareTo(Constante.MONTO_REIIRO_MAXIMO) >0) {
				
				return mensaje.setMensaje(Constante.RETIRO_DIARIO_LIMITE);
			}
			if (movimientos.isEmpty()) {// Si no hay movimientos
				if (requestMovimiento.getValor().compareTo(BigDecimal.ZERO)<0) {
					if (requestMovimiento.getValor().compareTo(cuenta.getSaldoInicial()) > 0) { // Mayor retiro que saldo inicial																						// saldo incial
						return mensaje.setMensaje(Constante.SALDO_NO_DISPONIBLE);
					} else {			
						movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
								.saldo(cuenta.getSaldoInicial().add(requestMovimiento.getValor()))
								.tipo(ETipoMovimiento.RETIRO).valor(requestMovimiento.getValor()).build();
						movimientoRepository.save(movimiento);
						return mensaje.setMensaje(Constante.TRANSACCION_EXITOSA);
					}
				} else { // DEPOSITO
					movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
							.saldo(requestMovimiento.getValor().add(cuenta.getSaldoInicial()))
							.tipo(ETipoMovimiento.DEPOSITO).valor(requestMovimiento.getValor()).build();
					movimientoRepository.save(movimiento);
					return mensaje.setMensaje(Constante.TRANSACCION_EXITOSA);
				}
			} else { // Movimientos
				Movimiento ultimoMovimiento = movimientos.get(movimientos.size() - 1);
				if (requestMovimiento.getValor().compareTo(BigDecimal.ZERO)<0) {
					if (requestMovimiento.getValor().compareTo(ultimoMovimiento.getSaldo()) > 0) { // Mayor retiro que
																								// saldO
						return mensaje.setMensaje(Constante.SALDO_NO_DISPONIBLE);
					} else {
						movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
								.saldo(ultimoMovimiento.getSaldo().add(requestMovimiento.getValor()))
								.tipo(ETipoMovimiento.RETIRO).valor(requestMovimiento.getValor()).build();
						movimientoRepository.save(movimiento);
						return mensaje.setMensaje(Constante.TRANSACCION_EXITOSA);
					}
				} else { // DEPOSITO
					movimiento = Movimiento.builder().cuenta(cuenta).fecha(requestMovimiento.getFecha())
							.saldo(requestMovimiento.getValor().add(ultimoMovimiento.getSaldo()))
							.tipo(ETipoMovimiento.DEPOSITO).valor(requestMovimiento.getValor()).build();
					movimientoRepository.save(movimiento);
					return mensaje.setMensaje(Constante.TRANSACCION_EXITOSA);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	public List<CuentaDto> getAllMovimientoByClienteId( Long clienteId, Date startDate, Date  endDate  ) {
		System.out.println(clienteId);
		System.out.println(startDate);
		System.out.println(endDate);
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			List<Movimiento>  movimientos = null;
			if (clienteId == null)
				return null;
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				movimientos = movimientoRepository.fecthMovimientoBetweenDatesAndClientID(startDate, endDate, clienteId);
			 List<CuentaDto> listaReporte= movimientos.stream().map(movimiento -> {
				 CuentaDto reporte;
				reporte = CuentaDto.builder()
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
			
			if (listaReporte == null) {
				return null;
			}
			return listaReporte;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	
	
	
	
	

}
