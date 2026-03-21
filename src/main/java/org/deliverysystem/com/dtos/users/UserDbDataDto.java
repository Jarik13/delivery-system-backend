package org.deliverysystem.com.dtos.users;

public record UserDbDataDto(
        Integer id,
        String middleName,
        String phoneNumber,
        Integer branchId,
        String licenseNumber
) {
    public static UserDbDataDto of(Integer id, String middleName, String phoneNumber) {
        return new UserDbDataDto(id, middleName, phoneNumber, null, null);
    }

    public static UserDbDataDto ofEmployee(Integer id, String middleName, String phoneNumber, Integer branchId) {
        return new UserDbDataDto(id, middleName, phoneNumber, branchId, null);
    }

    public static UserDbDataDto ofDriver(Integer id, String middleName, String phoneNumber, String licenseNumber) {
        return new UserDbDataDto(id, middleName, phoneNumber, null, licenseNumber);
    }
}