package com.ms_pedidos_pagos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CrearPedidoPagoDTO {

    private String usuarioId;
    private String direccionId;
    private String metodoPago; // "WEB", "TRANSFERENCIA", etc.
    private BigDecimal total;  // total calculado en el frontend
    private List<ItemPedidoDTO> items;
}