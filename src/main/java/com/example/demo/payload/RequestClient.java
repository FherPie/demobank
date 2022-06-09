package com.example.demo.payload;

import java.util.List;

import com.example.demo.modelo.EEstado;
import com.example.demo.modelo.EGenero;

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
public class RequestClient {

	private String password;

	private EEstado estado;

	private String nombre;

	private EGenero genero;

	private int edad;

	private String identificacion;

	private String direccion;

	private String telefono;

	private String email;

	private String apellido;

	private Long clienteId;

	private List<RequestCuenta> cuentas;

	private RequestCuenta cuenta;

}
