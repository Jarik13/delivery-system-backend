package org.deliverysystem.com.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessage {
    public static final String ENTITY_NOT_FOUND_BY_ID = "Запис не знайдено з ID: ";
    public static final String ENTITY_NOT_FOUND_FOR_UPDATE = "Неможливо оновити. Запис не знайдено.";
    public static final String ENTITY_NOT_FOUND_FOR_DELETE = "Неможливо видалити. Запис не знайдено.";

    public static final String CLIENT_NOT_FOUND_BY_PHONE = "Клієнта з таким номером телефону не знайдено: ";

    public static final String VEHICLE_NOT_FOUND_BY_PLATE = "Транспортний засіб з таким номером не знайдено: ";

    public static final String SHIPMENT_NOT_FOUND_BY_TRACKING = "Відправлення з трек-номером не знайдено: ";

    public static final String RETURN_NOT_FOUND_BY_TRACKING = "Повернення з трек-номером не знайдено: ";

    public static final String PAYMENT_NOT_FOUND_BY_TRANSACTION = "Платіж з номером транзакції не знайдено: ";

    public static final String WAYBILL_NOT_FOUND_BY_NUMBER = "Накладна з номером не знайдена: ";
}