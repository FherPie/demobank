package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Cuenta;

@Repository
@Transactional(readOnly = true)
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("select c from Cuenta c where cliente =: cliente")
	List<Cuenta> findByCliente(Cliente cliente);
	
  
    @Query("SELECT "
            + "DISTINCT (cuenta) from Cuenta cuenta "
            + " INNER JOIN  "
            + "cuenta.cliente cliente  "
            + "WHERE "
            + "cliente.id = :clienteId")
    List<Cuenta> fecthByCliente(long clienteId);
		
    
    @Query("SELECT "
            + "DISTINCT (cuenta) from Cuenta cuenta "
            + " INNER JOIN  "
            + "cuenta.cliente cliente  ")
    List<Cuenta> fecthCuentas();
    
    
    
    List<Cuenta> findAllByNumeroCuenta(String numeroCuenta);
    
    
    //TO DO CUENTA CON TODOS LOS MOVIMIENTOS
//	  @Query("SELECT "
//	            + "DISTINCT (cliente) from Cliente cliente "
//	            + "JOIN FETCH "
//	            + "cliente.cuentas  "
//	            + "WHERE "
//	            + "cliente.id = :clienteId")
//	 Cliente fetchClienteWithCuentasByIdentification(long clienteId);
//	
}
