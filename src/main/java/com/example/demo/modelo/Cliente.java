package com.example.demo.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class Cliente  implements Serializable {
	

	private static final long serialVersionUID = 7798845879900067882L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "password")
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
    private EEstado estado;
	
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name="persona_Id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Persona persona;

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JoinColumn(name="cliente_id")
	private Set<Cuenta> cuentas= new HashSet<Cuenta>();

	public Cliente addCuenta(Cuenta cuenta) {
		this.cuentas.add(cuenta);
	     cuenta.setCliente(this);
	     return this;
	}
	
	public Cliente removeCuenta(Cuenta cuenta) {
		this.cuentas.remove(cuenta);
	     cuenta.setCliente(null);
	     return this;
	}
	

	
	public Cliente(String password, EEstado estado, Persona persona) {
		super();
		this.password = password;
		this.estado = estado;
		this.persona = persona;
	}

	@Override
	public String toString() {
		return "Cliente [clienteId=" + id + ", password=" + password + ", estado=" + estado + ", persona="
				+ persona + "]";
	}
	
	
	
	
}
