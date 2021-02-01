package com.emersonluiz.DsClient.services;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emersonluiz.DsClient.dto.ClientDTO;
import com.emersonluiz.DsClient.entities.Client;
import com.emersonluiz.DsClient.repositories.ClientRepository;
import com.emersonluiz.DsClient.services.exceptions.DatabaseException;
import com.emersonluiz.DsClient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
		
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(entity);
	}

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		

		entity = repository.save(entity);
		return new ClientDTO(entity);
	}

	@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
		Client entity = repository.getOne(id);
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		
		entity = repository.save(entity);
		return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

		
	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
		throw new ResourceNotFoundException("Id not found " + id);
		
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}	
}

