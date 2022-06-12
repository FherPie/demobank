package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.RequestClientDto;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Persona;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.PersonaRepository;
import com.example.demo.services.ClientService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ClienteController {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	PersonaRepository personaRepository;

	@Autowired
	ClientService clienteService;
	
	
	@GetMapping("/clients")
	  public ResponseEntity<List<RequestClientDto>> getAllClientesByIdentification(@RequestParam(required = false) String identification) {	
		System.out.println(identification);

	    try {
	      List<Cliente> clients = new ArrayList<Cliente>();
	      if (identification == null || identification.isEmpty())
	    	 clienteRepository.findAll().forEach(clients::add);
	      else
	    	  clienteRepository.findClienteByIdentification(identification).forEach(clients::add);
	      
	      List<RequestClientDto> clientesDto=clients.stream().map(x-> {
	    	  RequestClientDto clienteDto= RequestClientDto.builder().nombre(x.getPersona().getNombre()+" " +x.getPersona().getApellido())
	    			  .direccion(x.getPersona().getDireccion())
	    			  .telefono(x.getPersona().getTelefono())
	    			  .password(x.getPassword())
	    	          .estado(x.getEstado()).build();
	    	  return clienteDto;
	      }).collect(Collectors.toList());;
	      
	      
	      if (clientesDto.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(clientesDto, HttpStatus.OK);
	    } catch (Exception e) {
	    	e.printStackTrace();
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	@GetMapping("/clients/{id}")
	public ResponseEntity<Cliente> getClientById(@PathVariable("id") long id) {
		Optional<Cliente> tutorialData = clienteRepository.findById(id);
		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/clients")
	public ResponseEntity<Cliente> createClient(@RequestBody RequestClientDto requestClient) {
		try {
			return new ResponseEntity<>(clienteService.crearCliente(requestClient), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/clients/{id}")
	public ResponseEntity<Cliente> updateCliente(@PathVariable("id") long id, @RequestBody Cliente client) {

		try {

			Optional<Cliente> clientData = clienteRepository.findById(id);
			if (clientData.isPresent()) {
				System.out.println(client);
				System.out.println(clientData.get());
				Persona person = clientData.get().getPersona();
				person.setNombre(client.getPersona().getNombre());
				person.setApellido(client.getPersona().getApellido());
				person.setDireccion(client.getPersona().getDireccion());
				person.setEmail(client.getPersona().getEmail());
				person.setIdentificacion(client.getPersona().getIdentificacion());
				person.setTelefono(client.getPersona().getTelefono());
				person.setGenero(client.getPersona().getGenero());
				Cliente cliente= clientData.get();
				cliente.setEstado(client.getEstado());
				cliente.setPassword(client.getPassword());
				personaRepository.save(person);
				clienteRepository.save(cliente);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/clients/{id}")
	public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") long id) {
		try {
			clienteRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/clients")
	public ResponseEntity<HttpStatus> deleteAllClients() {
		try {
			clienteRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}