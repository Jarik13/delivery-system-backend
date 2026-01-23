package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.ClientDto;
import org.deliverysystem.com.entities.Client;
import org.deliverysystem.com.mappers.ClientMapper;
import org.deliverysystem.com.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService extends AbstractBaseService<Client, ClientDto, Integer> {
    public ClientService(ClientRepository repository, ClientMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public ClientDto findByPhoneNumber(String phoneNumber) {
        return ((ClientRepository) repository).findByPhoneNumber(phoneNumber).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CLIENT_NOT_FOUND_BY_PHONE + phoneNumber));
    }
}