package org.deliverysystem.com.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.deliverysystem.com.constants.ErrorMessage;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.entities.Waybill;
import org.deliverysystem.com.mappers.WaybillMapper;
import org.deliverysystem.com.repositories.WaybillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WaybillService extends AbstractBaseService<Waybill, WaybillDto, Integer> {
    private final WaybillRepository waybillRepository;

    public WaybillService(WaybillRepository repository, WaybillMapper mapper) {
        super(mapper, repository);
        this.waybillRepository = repository;
    }

    @Transactional(readOnly = true)
    public WaybillDto findByNumber(Integer number) {
        return waybillRepository.findByNumber(number).map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.WAYBILL_NOT_FOUND_BY_NUMBER + number));
    }

    @Transactional(readOnly = true)
    public List<WaybillDto> findAllForExport(Integer number) {
        if (number != null) {
            return waybillRepository.findByNumber(number).map(w -> List.of(mapper.toDto(w))).orElse(List.of());
        }
        return waybillRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<WaybillDto> findAllByIds(List<Integer> ids) {
        return waybillRepository.findAllById(ids).stream().map(mapper::toDto).toList();
    }
}