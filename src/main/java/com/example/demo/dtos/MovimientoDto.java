package com.example.demo.dtos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

import javax.validation.constraints.NotNull;

import com.example.demo.modelo.ETipoMovimiento;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter @NoArgsConstructor 
@AllArgsConstructor
public class MovimientoDto {

	private long id;
	private String numeroCuenta;
	@NotNull
    private ETipoMovimiento tipo;
    private BigDecimal saldo;
    @NotNull
    private BigDecimal valor;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es_ES", timezone = "America/Guayaquil")
	private Date fecha;
	private Long cuentaId;
	

		 
}
