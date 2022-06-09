package com.example.demo.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Entity
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class Cuenta  implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1558291680885102669L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long cuentaId;
	
	@Column(name = "numeroCuenta")
	private String numeroCuenta;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoCuenta")
    private ETipoCuenta tipoCuenta;
	
	@Column(name = "saldoInicial")
    private BigDecimal saldoInicial;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
    private EEstado estado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonIgnore
	private Cliente cliente;
	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy="cuenta", orphanRemoval = true)
	private List<Movimiento> movimientos;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cuentaId ^ (cuentaId >>> 32));
		result = prime * result + ((numeroCuenta == null) ? 0 : numeroCuenta.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta other = (Cuenta) obj;
		if (cuentaId != other.cuentaId)
			return false;
		if (numeroCuenta == null) {
			if (other.numeroCuenta != null)
				return false;
		} else if (!numeroCuenta.equals(other.numeroCuenta))
			return false;
		return true;
	}

}
