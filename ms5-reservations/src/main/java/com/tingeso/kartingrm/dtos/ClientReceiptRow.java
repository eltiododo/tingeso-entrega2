package com.tingeso.kartingrm.dtos;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ClientReceiptRow {
    String clientName;
    Integer baseTariff;
    QuantityDiscount groupDiscount;
    GenericDiscountType individualDiscount;
    Integer finalCost;
}
