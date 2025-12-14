package com.ms_pedidos_pagos.controller;

import com.ms_pedidos_pagos.dto.ComprobantePagoDTO;
import com.ms_pedidos_pagos.dto.CrearPedidoPagoDTO;
import com.ms_pedidos_pagos.dto.ActualizarEstadoPedidoDTO;
import com.ms_pedidos_pagos.dto.PedidoDetalleDTO;
import com.ms_pedidos_pagos.model.Pedido;
import com.ms_pedidos_pagos.service.PedidoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ms_pedidos_pagos.repository.PedidoRepository;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoPagoController {

    private final PedidoPagoService pedidoPagoService;
    private final PedidoRepository pedidoRepository;

    // POST /pedidos/pagar
    @PostMapping("/pagar")
    public ComprobantePagoDTO pagar(@RequestBody CrearPedidoPagoDTO dto) {
        return pedidoPagoService.crearPedidoYPago(dto);
    }

    // GET listado general (Admin / Vendedor)
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPorUsuario(@PathVariable Long usuarioId) {
    return pedidoRepository.findByUsuarioId(usuarioId);
    }

    // GET busca el pedido por ID 
    @GetMapping("/{pedidoId}")
    public Pedido buscarPorId(@PathVariable String pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido con ID " + pedidoId + " no encontrado"));
    }

    // PATCH actualizar estado (Admin)
    @PatchMapping("/{pedidoId}/estado")
    public Pedido actualizarEstado(@PathVariable String pedidoId, @RequestBody ActualizarEstadoPedidoDTO dto) {
        return pedidoPagoService.actualizarEstado(pedidoId, dto.getEstado());
    }

    // GET detalle del pedido con items + metodoPago
    @GetMapping("/{pedidoId}/detalle")
    public PedidoDetalleDTO detalle(@PathVariable String pedidoId) {
        return pedidoPagoService.getDetalle(pedidoId);
    }

}