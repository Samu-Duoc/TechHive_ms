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
import com.ms_pedidos_pagos.webclient.CarritoClient;
import com.ms_pedidos_pagos.webclient.ProductoClient;
import com.ms_pedidos_pagos.webclient.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoPagoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PagoRepository pagoRepository;

    private final UsuarioClient usuarioClient;
    private final CarritoClient carritoClient;
    private final ProductoClient productoClient;

    @Transactional
    public ComprobantePagoDTO crearPedidoYPago(CrearPedidoPagoDTO dto) {

        // 1) Validar usuario en ms_auth_usuarios
        Long usuarioId = dto.getUsuarioId(); 
        Map<String, Object> usuario = usuarioClient.getUsuarioById(usuarioId);
        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado. No se puede crear el pedido.");
        }

        // 2) Obtener o crear carrito (para que no se caiga si BD nueva está vacía)
        Map<String, Object> carrito = carritoClient.getOrCreateCarritoByUsuarioId(usuarioId);
        if (carrito == null || carrito.isEmpty()) {
            throw new RuntimeException("No se pudo obtener/crear el carrito para usuarioId=" + usuarioId);
        }

        // 3) Crear Pedido
        Pedido pedido = new Pedido();
        pedido.setPedidoId(UUID.randomUUID().toString());

        //si tu Pedido.usuarioId ahora es Long, usa esto:
        pedido.setUsuarioId(usuarioId);

        pedido.setDireccionId(dto.getDireccionId());
        pedido.setTotal(dto.getTotal());
        pedido.setEstado("PAGADO");
        pedido.setFecha(LocalDateTime.now());

        pedido = pedidoRepository.save(pedido);

        // 4) Detalles + stock
        for (ItemPedidoDTO item : dto.getItems()) {

            Long productoId = item.getProductoId(); 

            // valida producto (si no existe, lanza excepción clara)
            productoClient.getProductoById(productoId);

            DetallePedido det = new DetallePedido();
            det.setDetallePId(UUID.randomUUID().toString());
            det.setPedido(pedido);
            det.setProductoId(productoId);

            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(item.getPrecioUnitario());

            BigDecimal subtotal = item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
            det.setSubtotal(subtotal);

            detallePedidoRepository.save(det);

            // descontar stock usando endpoint real /{id}/stock
            productoClient.descontarStock(productoId, item.getCantidad());
        }

        // 5) Pago
        Pago pago = new Pago();
        pago.setPagosId(UUID.randomUUID().toString());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("APROBADO");
        pago.setMonto(dto.getTotal());
        pagoRepository.save(pago);

        // 6) Comprobante
        ComprobantePagoDTO comprobante = new ComprobantePagoDTO();
        comprobante.setMensaje("Compra realizada con éxito");
        comprobante.setPedidoId(pedido.getPedidoId());
        comprobante.setFecha(pedido.getFecha());
        comprobante.setTotal(pedido.getTotal());
        comprobante.setMetodoPago(pago.getMetodoPago());

        return comprobante;
    }
}
