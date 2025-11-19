package com.ms_pedidos_pagos.repository;

import com.ms_pedidos_pagos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, String> {
}
