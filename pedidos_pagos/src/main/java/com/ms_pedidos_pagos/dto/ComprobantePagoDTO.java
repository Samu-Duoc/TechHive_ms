package com.ms_pedidos_pagos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ComprobantePagoDTO {

    private String mensaje;      // "Compra realizada con éxito"
    private String pedidoId;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String metodoPago;
}
