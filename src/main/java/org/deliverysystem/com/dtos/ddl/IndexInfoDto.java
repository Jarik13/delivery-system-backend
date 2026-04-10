package org.deliverysystem.com.dtos.ddl;

import java.util.List;

public record IndexInfoDto(
        String indexName,
        List<String> columns,
        boolean isUnique
) {}