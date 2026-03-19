package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.clients.ClientDto;
import org.deliverysystem.com.dtos.clients.CreateClientDto;
import org.deliverysystem.com.entities.Client;
import org.deliverysystem.com.mappers.ClientMapper;
import org.deliverysystem.com.repositories.ClientRepository;
import org.deliverysystem.com.utils.RestPage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public RestPage<ClientDto> findAll(Pageable pageable) {
        return new RestPage<>(clientRepository.findAll(pageable).map(clientMapper::toDto));
    }

    @Transactional(readOnly = true)
    public ClientDto findById(Integer id) {
        return clientMapper.toDto(getOrThrow(id));
    }

    @Transactional
    public ClientDto createClient(CreateClientDto dto) {
        Client client = new Client();
        client.setFirstName(dto.firstName());
        client.setLastName(dto.lastName());
        client.setMiddleName(dto.middleName());
        client.setPhoneNumber(dto.phoneNumber());
        client.setEmail(dto.email());
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Transactional
    public ClientDto update(Integer id, CreateClientDto dto) {
        Client client = getOrThrow(id);
        client.setFirstName(dto.firstName());
        client.setLastName(dto.lastName());
        client.setMiddleName(dto.middleName());
        client.setPhoneNumber(dto.phoneNumber());
        client.setEmail(dto.email());
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Transactional
    public void delete(Integer id) {
        clientRepository.delete(getOrThrow(id));
    }

    private Client getOrThrow(Integer id) {
        return clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Клієнта не знайдено: " + id));
    }
}