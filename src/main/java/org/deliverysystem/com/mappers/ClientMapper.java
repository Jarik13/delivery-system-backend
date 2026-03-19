package org.deliverysystem.com.mappers;

import org.deliverysystem.com.dtos.clients.ClientDto;
import org.deliverysystem.com.entities.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends GenericMapper<Client, ClientDto> {
}