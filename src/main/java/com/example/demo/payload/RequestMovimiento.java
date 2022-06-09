package com.example.demo.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.example.demo.modelo.ETipoMovimiento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter @NoArgsConstructor 
@AllArgsConstructor
public class RequestMovimiento {

	private long id;
	private String numeroCuenta;
	@NotNull
    private ETipoMovimiento tipo;
    private BigDecimal saldo;
    @NotNull
    private BigDecimal valor;
	private LocalDateTime fecha;
	private Long cuentaId;
	

		 
}
