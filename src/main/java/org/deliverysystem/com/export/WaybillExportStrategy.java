package org.deliverysystem.com.export;

import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WaybillExportStrategy {
    ResponseEntity<byte[]> export(List<WaybillDto> waybills);
    String getFormat();
}