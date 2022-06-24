package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensajeMovimientoDto {
	
	private String mensaje;
	
	
	public String getMensaje() {
		return mensaje;
	}

	public MensajeMovimientoDto setMensaje(String mensaje) {
		this.mensaje = mensaje;
		return this;
	}

}
