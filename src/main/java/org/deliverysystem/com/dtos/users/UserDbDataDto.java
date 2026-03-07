package org.deliverysystem.com.dtos.users;

public record UserDbDataDto(
        Integer id,
        String middleName,
        String phoneNumber
) {}