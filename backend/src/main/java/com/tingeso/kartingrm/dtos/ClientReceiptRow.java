package com.tingeso.kartingrm.dtos;

import com.tingeso.kartingrm.enums.DiscountType;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ClientReceiptRow {
    String clientName;
    Integer baseTariff;
    DiscountType groupDiscount;
    DiscountType individualDiscount;
    Integer finalCost;
}
