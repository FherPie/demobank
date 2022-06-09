package com.example.demo.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor // <--- THIS is it
public class Persona  implements Serializable {
	

	private static final long serialVersionUID = 7798845879900067882L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long personaId;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "apellido")
	private String apellido;
	
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
    private EGenero genero;
	
	@Column(name = "edad")
	private int edad;
	
	@Column(name = "identificacion")
	private String identificacion;
	
	@Column(name = "direccion")
	private String direccion;
	
	@Column(name = "telefono")
	private String telefono;
	
	@Column(name = "email")
	private String email;

	public Persona(String nombre, EGenero genero, int edad, String identificacion, String direccion, String telefono,
			String email, String apellido) {
		super();
		this.nombre = nombre;
		this.genero = genero;
		this.edad = edad;
		this.identificacion = identificacion;
		this.direccion = direccion;
		this.telefono = telefono;
		this.email = email;
		this.apellido=apellido;
	}

	@Override
	public String toString() {
		return "Persona [personaId=" + personaId + ", nombre=" + nombre + ", genero=" + genero + ", edad=" + edad
				+ ", identificacion=" + identificacion + ", direccion=" + direccion + ", telefono=" + telefono
				+ ", email=" + email + "]";
	}
	

	
	

}
