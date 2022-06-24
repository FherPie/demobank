package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CuentaDto {

	private String fecha;
	private String nombreCliente;
	private String numeroCuenta;
	private String tipoCuenta;
	private String saldoInicial;
	private String estado;
	private String movimiento;
	private String saldoDisponible;
}
