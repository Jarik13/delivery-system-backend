package org.deliverysystem.com.export;

import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RouteListExportStrategy {
    String getFormat();
    ResponseEntity<byte[]> export(List<RouteListDto> routeLists);
}