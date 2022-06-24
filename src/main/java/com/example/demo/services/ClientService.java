package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dtos.ClientDto;
import com.example.demo.modelo.Cliente;
import com.example.demo.modelo.Persona;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.PersonaRepository;

@Service
public class ClientService {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	PersonaRepository personaRepository;

	@Transactional
	public Cliente crearCliente(ClientDto requestClient) {
		Persona persona = new Persona(requestClient.getNombre(), requestClient.getGenero(), requestClient.getEdad(),
				requestClient.getIdentificacion(), requestClient.getDireccion(), requestClient.getTelefono(),
				requestClient.getEmail(), requestClient.getApellido());
		personaRepository.save(persona);

		Cliente cliente = new Cliente();
		cliente.setPassword(requestClient.getPassword());
		cliente.setEstado(requestClient.getEstado());
		cliente.setPersona(persona);

		return clienteRepository.save(cliente);

	}

	@Transactional(readOnly = true)
	public List<ClientDto> getAllClientesByIdentification(String identification) {
		System.out.println(identification);
		List<ClientDto> clientesDto = null;
		try {
			List<Cliente> clients = new ArrayList<Cliente>();
			if (Objects.isNull(identification) || identification.isEmpty()) {
				clienteRepository.findAll().forEach(clients::add);
			}
			else {
				clienteRepository.findClienteByIdentification(identification).forEach(clients::add);
			}
			clientesDto = clients.stream().map(x -> {
				ClientDto clienteDto = ClientDto.builder()
						.nombre(x.getPersona().getNombre() + " " + x.getPersona().getApellido())
						.direccion(x.getPersona().getDireccion()).telefono(x.getPersona().getTelefono())
						.password(x.getPassword()).estado(x.getEstado()).clienteId(x.getId()).build();
				return clienteDto;
			}).collect(Collectors.toList());
			;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return clientesDto;
	}

   @Transactional
	public Cliente updateCliente(long id, Cliente client) {
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
				Cliente cliente = clientData.get();
				cliente.setEstado(client.getEstado());
				cliente.setPassword(client.getPassword());
				personaRepository.save(person);
				clienteRepository.save(cliente);
				return clienteRepository.save(cliente); 
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}

}
