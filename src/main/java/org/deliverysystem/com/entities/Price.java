package org.deliverysystem.com.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    @Column(name = "shipment_price_for_delivery")
    private BigDecimal delivery;

    @Column(name = "shipment_price_for_weight")
    private BigDecimal weight;

    @Column(name = "shipment_price_for_distance")
    private BigDecimal distance;

    @Column(name = "shipment_price_for_box_variant")
    private BigDecimal boxVariant;

    @Column(name = "shipment_price_for_special_packaging")
    private BigDecimal specialPackaging;

    @Column(name = "shipment_total_price")
    private BigDecimal total;

    @Column(name = "shipment_insurance_fee")
    private BigDecimal insuranceFee;
}
