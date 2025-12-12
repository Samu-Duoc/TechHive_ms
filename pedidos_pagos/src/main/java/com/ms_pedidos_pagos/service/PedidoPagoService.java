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
import com.ms_pedidos_pagos.webclient.UsuarioClient;
import com.ms_pedidos_pagos.webclient.CarritoClient;
import com.ms_pedidos_pagos.webclient.ProductoClient;
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

    //nuevos clientes WebClient al estilo del profe
    private final UsuarioClient usuarioClient;
    private final CarritoClient carritoClient;
    private final ProductoClient productoClient;

    @Transactional
    public ComprobantePagoDTO crearPedidoYPago(CrearPedidoPagoDTO dto) {

        // 1) Validar usuario en ms_auth_usuarios
        Long usuarioId = Long.parseLong(dto.getUsuarioId());
        Map<String, Object> usuario = usuarioClient.getUsuarioById(usuarioId);
        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado. No se puede crear el pedido.");
        }

        // (Opcional: podrías validar estado = "Activo", rol, etc. leyendo el Map)

        //Obtener carrito desde ms_carrito (si quieres usarlo como respaldo)
        Map<String, Object> carrito = carritoClient.getCarritoByUsuarioId(usuarioId);
        if (carrito == null || carrito.isEmpty()) {
            throw new RuntimeException("El usuario no tiene carrito. No se puede crear el pedido.");
        }

        // Crear Pedido
        Pedido pedido = new Pedido();
        pedido.setPedidoId(UUID.randomUUID().toString());
        pedido.setUsuarioId(dto.getUsuarioId());
        pedido.setDireccionId(dto.getDireccionId());
        pedido.setTotal(dto.getTotal());
        pedido.setEstado("PAGADO");
        pedido.setFecha(LocalDateTime.now());

        pedido = pedidoRepository.save(pedido);

        //Crear DetallePedido por cada ítem y descontar stock en productos
        for (ItemPedidoDTO item : dto.getItems()) {

            Long productoId = Long.parseLong(item.getProductoId());

            //validar producto y stock actual
            Map<String, Object> producto = productoClient.getProductoById(productoId);
            // aquí podrías leer "stock" desde el Map y comparar con item.getCantidad()

            DetallePedido det = new DetallePedido();
            det.setDetallePId(UUID.randomUUID().toString());
            det.setPedido(pedido);
            det.setProductoId(item.getProductoId());
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(item.getPrecioUnitario());

            BigDecimal subtotal = item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
            det.setSubtotal(subtotal);

            detallePedidoRepository.save(det);

            // Descontar stock en ms_productos_categorias
            productoClient.descontarStock(productoId, item.getCantidad());
        }

        //Crear registro de pago
        Pago pago = new Pago();
        pago.setPagosId(UUID.randomUUID().toString());
        pago.setPedidoId(pedido.getPedidoId());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("APROBADO");
        pago.setMonto(dto.getTotal());
        pagoRepository.save(pago);

        // Armar comprobante para el frontend
        ComprobantePagoDTO comprobante = new ComprobantePagoDTO();
        comprobante.setMensaje("Compra realizada con éxito");
        comprobante.setPedidoId(pedido.getPedidoId());
        comprobante.setFecha(pedido.getFecha());
        comprobante.setTotal(pedido.getTotal());
        comprobante.setMetodoPago(pago.getMetodoPago());

        return comprobante;
    }
}
