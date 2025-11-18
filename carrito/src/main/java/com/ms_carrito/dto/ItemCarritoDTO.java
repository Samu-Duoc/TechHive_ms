package com.ms_carrito.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarritoDTO {

    private Long id;  // id del detalle
    private String productoId;
    private Integer cantidad;
    private Double subtotal;
}
