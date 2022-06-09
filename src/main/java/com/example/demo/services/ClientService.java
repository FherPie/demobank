package com.example.demo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Persona;
import com.example.demo.payload.RequestClient;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.PersonaRepository;

@Service
public class ClientService {
	
	  @Autowired
	  ClienteRepository clienteRepository;
	  
	  @Autowired
	  PersonaRepository personaRepository;
	  
	  @Transactional
	  public Cliente  crearCliente(RequestClient requestClient) {
		   	Persona persona= new Persona(requestClient.getNombre(), requestClient.getGenero(), requestClient.getEdad(),requestClient.getIdentificacion()
	    			,requestClient.getDireccion(), requestClient.getTelefono(), requestClient.getEmail(), requestClient.getApellido());
		   	personaRepository.save(persona);
		   	
		   	Cliente cliente= new Cliente();
		   	cliente.setPassword(requestClient.getPassword());
		   	cliente.setEstado(requestClient.getEstado());
		   	cliente.setPersona(persona);
		  
		return clienteRepository.save(cliente);
		  
	  }
		  
		  
	  
}
