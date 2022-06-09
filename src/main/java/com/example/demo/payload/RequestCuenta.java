package com.example.demo.payload;

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
@NoArgsConstructor // <--- THIS is it
@AllArgsConstructor
public class RequestCuenta {
	


	private String numeroCuenta;
    private ETipoCuenta tipoCuenta;
    @NotNull
    private BigDecimal saldoInicial;
    private EEstado estado;

}
