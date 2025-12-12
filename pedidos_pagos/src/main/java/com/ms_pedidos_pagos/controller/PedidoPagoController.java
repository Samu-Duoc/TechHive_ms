package com.ms_pedidos_pagos.controller;

import com.ms_pedidos_pagos.dto.ComprobantePagoDTO;
import com.ms_pedidos_pagos.dto.CrearPedidoPagoDTO;
import com.ms_pedidos_pagos.model.Pedido;
import com.ms_pedidos_pagos.service.PedidoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.ms_pedidos_pagos.repository.PedidoRepository;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
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
    public List<Pedido> listarPorUsuario(@PathVariable String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
}