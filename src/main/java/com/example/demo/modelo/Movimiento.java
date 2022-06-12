package com.example.demo.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter 
@Builder
@AllArgsConstructor
@NoArgsConstructor 
public class Movimiento  implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1558291680885102669L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(AccessLevel.PROTECTED)
	private long movimientoId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo")
    private ETipoMovimiento tipo;

	@Column(name = "saldo")
    private BigDecimal saldo;
	
	@Column(name = "valor")
    private BigDecimal valor;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha")
	private Date fecha;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="cuenta_Id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonIgnore
	private Cuenta cuenta;

	@Override
	public String toString() {
		return "Movimiento [movimientoId=" + movimientoId + ", tipo=" + tipo + ", saldo=" + saldo + ", valor=" + valor
				+ ", fecha=" + fecha + ", cuenta=" + cuenta + "]";
	}
	
	

}
