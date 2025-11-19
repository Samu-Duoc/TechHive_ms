package com.ms_pedidos_pagos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
public class Pago {

    @Id
    @Column(name = "pagos_id")
    private String pagosId;

    @Column(name = "pedido_id", nullable = false)
    private String pedidoId;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago; // "WEB"

    @Column(nullable = false)
    private String estado; // "PAGADO"

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private BigDecimal monto;
}
