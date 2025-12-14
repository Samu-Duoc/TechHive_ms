package com.ms_pedidos_pagos.repository;

import com.ms_pedidos_pagos.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, String> {
    List<DetallePedido> findByPedido_PedidoId(String pedidoId);
}