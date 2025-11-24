package com.ms_pedidos_pagos.controller;

import com.ms_pedidos_pagos.dto.ComprobantePagoDTO;
import com.ms_pedidos_pagos.dto.CrearPedidoPagoDTO;
import com.ms_pedidos_pagos.service.PedidoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoPagoController {

    private final PedidoPagoService pedidoPagoService;

    // POST /pedidos/pagar
    @PostMapping("/pagar")
    public ComprobantePagoDTO pagar(@RequestBody CrearPedidoPagoDTO dto) {
        return pedidoPagoService.crearPedidoYPago(dto);
    }
}