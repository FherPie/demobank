package com.example.demo.dtos;

import java.util.List;

import com.example.demo.modelo.EEstado;
import com.example.demo.modelo.EGenero;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ClientDto {

	private String password;

	private EEstado estado;

	private String nombre;

	private EGenero genero;

	private Integer edad;

	private String identificacion;

	private String direccion;

	private String telefono;

	private String email;

	private String apellido;

	private Long clienteId;

	private List<RequestCuentaDto> cuentas;

	private RequestCuentaDto cuenta;

}
