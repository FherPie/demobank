package com.example.demo.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.example.demo.modelo.EEstado;
import com.example.demo.modelo.ETipoCuenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

 @Builder
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class RequestCuentaDto {
	
	private String numeroCuenta;
    private ETipoCuenta tipoCuenta;
    @NotNull
    private BigDecimal saldoInicial;
    private EEstado estado;
    private String cliente;
    private String cuentaId;
    private Long clienteId;
}
