package com.example.demo.dtos;

import lombok.Data;

@Data
public class ReporteCuentasDto {

	private String fechaReporte;
	private String nombreCliente;
	private String numeroCuenta;
	private String tipoCuenta;
	private String saldoInicial;
	private String estado;
	private String movimiento;
	private String saldoDisponible;
}
