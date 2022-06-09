package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
            + "DISTINCT (movimiento) from Movimiento movimiento "
            + " INNER JOIN  "
            + "movimiento.cuenta cuenta  "
            + "WHERE "
            + "cuenta.id = :cuentaId")
    List<Movimiento> fecthByCuentaId(long cuentaId);
		
	
}
