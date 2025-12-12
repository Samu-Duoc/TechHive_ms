package com.ms_carrito.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CarritoDTO {

    private Long id;
    private Long usuarioId;
    private LocalDateTime fechaCreacion;
    private BigDecimal total;
    private List<ItemCarritoDTO> items;

}
