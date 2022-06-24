package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.modelo.Cuenta;
import com.example.demo.modelo.Movimiento;

@Repository
@Transactional(readOnly = true)
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("select m from Movimiento m where cuenta =: cuenta")
	List<Movimiento> findByCuenta(Cuenta cuenta);
	
  
    @Query("SELECT "
            + " (movimiento) from Movimiento movimiento "
            + " INNER JOIN  "
            + "movimiento.cuenta cuenta  "
            + "WHERE "
            + "cuenta.id = :cuentaId")
    List<Movimiento> fecthByCuentaId(long cuentaId);
		
	
    
    @Query("SELECT "
            + " (movimiento) from Movimiento movimiento "
            + " INNER JOIN  "
            + "movimiento.cuenta cuenta  "
            + "WHERE "
            + "cuenta.numeroCuenta = :numeroCuenta")
    List<Movimiento> fecthByNumeroCuenta(String numeroCuenta);
		
    

    @Query("SELECT "
            + " (movimiento) from Movimiento movimiento "
            + " INNER JOIN  "
            + "movimiento.cuenta.cliente cliente  "
            + "WHERE "
            + "cliente.id = :clienteId  and movimiento.fecha BETWEEN :startDate AND :endDate")
    List<Movimiento> fecthMovimientoBetweenDatesAndClientID(@Param("startDate")Date startDate,@Param("endDate")Date endDate, long clienteId);
    
    
    @Query("SELECT "
            + " (movimiento) from Movimiento movimiento "
            + " INNER JOIN  "
            + "movimiento.cuenta cuenta  "
            + "WHERE "
            + "cuenta.id = :cuentaId and movimiento.fecha BETWEEN :startDate AND :endDate and movimiento.valor < 0")
    List<Movimiento> fecthByMovimientoBetweenDatesAndCuentaId(@Param("startDate")Date startDate,@Param("endDate")Date endDate,long cuentaId);
    

}
