package com.ms_pedidos_pagos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemPedidoDTO {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
