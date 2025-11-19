package com.ms_pedidos_pagos.service;

import com.ms_pedidos_pagos.dto.ComprobantePagoDTO;
import com.ms_pedidos_pagos.dto.CrearPedidoPagoDTO;
import com.ms_pedidos_pagos.dto.ItemPedidoDTO;
import com.ms_pedidos_pagos.model.DetallePedido;
import com.ms_pedidos_pagos.model.Pago;
import com.ms_pedidos_pagos.model.Pedido;
import com.ms_pedidos_pagos.repository.DetallePedidoRepository;
import com.ms_pedidos_pagos.repository.PagoRepository;
import com.ms_pedidos_pagos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoPagoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PagoRepository pagoRepository;

    @Transactional
    public ComprobantePagoDTO crearPedidoYPago(CrearPedidoPagoDTO dto) {

        // 1) Crear Pedido
        Pedido pedido = new Pedido();
        pedido.setPedidoId(UUID.randomUUID().toString());
        pedido.setUsuarioId(dto.getUsuarioId());
        pedido.setDireccionId(dto.getDireccionId());
        pedido.setTotal(dto.getTotal());
        pedido.setEstado("PAGADO"); // lo marcamos pagado de una (simple)
        pedido.setFecha(LocalDateTime.now());

        pedido = pedidoRepository.save(pedido);

        // 2) Crear DetallePedido por cada item
        for (ItemPedidoDTO item : dto.getItems()) {
            DetallePedido det = new DetallePedido();
            det.setDetallePId(UUID.randomUUID().toString());
            det.setPedido(pedido);
            det.setProductoId(item.getProductoId());
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(item.getPrecioUnitario());
            det.setSubtotal(
                    item.getPrecioUnitario().multiply(
                            java.math.BigDecimal.valueOf(item.getCantidad())
                    )
            );
            detallePedidoRepository.save(det);
        }

        // 3) Crear Pago asociado
        Pago pago = new Pago();
        pago.setPagosId(UUID.randomUUID().toString());
        pago.setPedidoId(pedido.getPedidoId());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado("PAGADO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setMonto(dto.getTotal());
        pagoRepository.save(pago);

        // 4) Armar comprobante para el frontend
        ComprobantePagoDTO comprobante = new ComprobantePagoDTO();
        comprobante.setMensaje("Compra realizada con éxito");
        comprobante.setPedidoId(pedido.getPedidoId());
        comprobante.setFecha(pedido.getFecha());
        comprobante.setTotal(pedido.getTotal());
        comprobante.setMetodoPago(pago.getMetodoPago());

        return comprobante;
    }
}
