package com.ms_pedidos_pagos.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDetalleDTO {
    private String pedidoId;
    private Long usuarioId;
    private String direccionId;
    private BigDecimal total;
    private String estado;
    private LocalDateTime fecha;
    private String metodoPago;
    private List<ItemDetalleDTO> items;
}
