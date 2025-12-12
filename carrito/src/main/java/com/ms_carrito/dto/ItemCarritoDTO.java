package com.ms_carrito.dto;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarritoDTO {

    private Long id;
    private String productoId;
    private Integer cantidad;
    private BigDecimal subtotal;
}
