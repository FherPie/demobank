package com.example.demo.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

import com.example.demo.dtos.ClientDto;
import com.example.demo.modelo.Cliente;
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
	  public ResponseEntity<List<ClientDto>> getAllClientesByIdentification( @Nullable @RequestParam(required = false) String identification) {	
		System.out.println(identification);
	    try {
	      List<ClientDto> clientesDto= clienteService.getAllClientesByIdentification(identification);
	      if (Objects.isNull(clientesDto) || clientesDto.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	      }
	      return new ResponseEntity<>(clientesDto, HttpStatus.OK);
	    } catch (Exception e) {
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
	public ResponseEntity<Cliente> createClient(@RequestBody ClientDto requestClient) {
		try {
			return new ResponseEntity<>(clienteService.crearCliente(requestClient), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/clients/{id}")
	public ResponseEntity<Cliente> updateCliente(@PathVariable("id") long id, @RequestBody Cliente client) {
		try {
			Cliente clientData = clienteService.updateCliente(id, client);
			if (Objects.nonNull(clientData)) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
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