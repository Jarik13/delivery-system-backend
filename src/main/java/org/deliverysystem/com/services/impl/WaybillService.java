package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.entities.Waybill;
import org.deliverysystem.com.mappers.WaybillMapper;
import org.deliverysystem.com.repositories.WaybillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WaybillService extends AbstractBaseService<Waybill, WaybillDto, Integer> {
    public WaybillService(WaybillRepository repository, WaybillMapper mapper) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public WaybillDto findByNumber(Integer number) {
        return ((WaybillRepository) repository).findByNumber(number).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.WAYBILL_NOT_FOUND_BY_NUMBER + number));
    }
}