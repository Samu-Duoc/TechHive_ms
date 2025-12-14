package com.ms_pedidos_pagos.service;

import com.ms_pedidos_pagos.dto.ComprobantePagoDTO;
import com.ms_pedidos_pagos.dto.CrearPedidoPagoDTO;
import com.ms_pedidos_pagos.dto.ItemDetalleDTO;
import com.ms_pedidos_pagos.dto.ItemPedidoDTO;
import com.ms_pedidos_pagos.dto.PedidoDetalleDTO;
import com.ms_pedidos_pagos.model.DetallePedido;
import com.ms_pedidos_pagos.model.Pago;
import com.ms_pedidos_pagos.model.Pedido;
import com.ms_pedidos_pagos.repository.DetallePedidoRepository;
import com.ms_pedidos_pagos.repository.PagoRepository;
import com.ms_pedidos_pagos.repository.PedidoRepository;
import com.ms_pedidos_pagos.webclient.ProductoClient;
import com.ms_pedidos_pagos.webclient.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoPagoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PagoRepository pagoRepository;

    private final UsuarioClient usuarioClient;
    private final ProductoClient productoClient;

    @Transactional
    public ComprobantePagoDTO crearPedidoYPago(CrearPedidoPagoDTO dto) {
        // 0) Validaciones básicas
        if (dto.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId es obligatorio");
        }
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido no puede venir sin items");
        }

        // 1) Validar usuario en ms_auth_usuarios
        Long usuarioId = dto.getUsuarioId();
        Map<String, Object> usuario = usuarioClient.getUsuarioById(usuarioId);
        if (usuario == null || usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuario no encontrado. No se puede crear el pedido.");
        }

        // 2) Validar stock antes de crear el pedido (agrupando cantidades por producto)
        Map<Long, Integer> qtyPorProducto = dto.getItems().stream()
                .collect(Collectors.groupingBy(
                        ItemPedidoDTO::getProductoId,
                        Collectors.summingInt(i -> i.getCantidad() == null ? 0 : i.getCantidad())
                ));

        for (Map.Entry<Long, Integer> entry : qtyPorProducto.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidadTotal = entry.getValue();

            if (productoId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productoId inválido");
            }
            if (cantidadTotal == null || cantidadTotal <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cantidad inválida para productoId=" + productoId);
            }

            Map<String, Object> producto = productoClient.getProductoById(productoId);
            Integer stock = extraerEntero(producto.get("stock"));
            if (stock == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "No pude leer stock del productoId=" + productoId + " (campo 'stock' no viene en la respuesta)");
            }

            if (cantidadTotal > stock) {
                String nombre = String.valueOf(producto.getOrDefault("name", "Producto"));
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Stock insuficiente: " + nombre + " (ID=" + productoId + "). Disponible=" + stock + ", solicitado=" + cantidadTotal
                );
            }
        }

        // 3) Crear Pedido (ya validado stock)
        Pedido pedido = new Pedido();
        pedido.setPedidoId(UUID.randomUUID().toString());
        pedido.setUsuarioId(usuarioId);
        pedido.setDireccionId(dto.getDireccionId());
        pedido.setTotal(dto.getTotal());
        pedido.setEstado("PAGADO");
        pedido.setFecha(LocalDateTime.now());

        pedido = pedidoRepository.save(pedido);

        // 4) Detalles
        for (ItemPedidoDTO item : dto.getItems()) {
            Long productoId = item.getProductoId();

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
        }

        // Descontar stock una sola vez por producto (cantidad total)
        for (Map.Entry<Long, Integer> entry : qtyPorProducto.entrySet()) {
            productoClient.descontarStock(entry.getKey(), entry.getValue());
        }

        // 5) Pago
        Pago pago = new Pago();
        pago.setPagosId(UUID.randomUUID().toString());
        pago.setPedidoId(pedido.getPedidoId());
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

    // Helper seguro para leer "stock" como Integer aunque venga Double/String/etc.
    private Integer extraerEntero(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof Double) return ((Double) value).intValue();
        if (value instanceof Float) return ((Float) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    public Pedido actualizarEstado(String pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pedido con ID " + pedidoId + " no encontrado"));

        // Validar que el estado sea válido
        String[] estadosValidos = {"CONFIRMADO", "PREPARANDO", "EN_TRANSITO", "ENTREGADO", "CANCELADO"};
        boolean estadoValido = false;
        for (String estado : estadosValidos) {
            if (estado.equals(nuevoEstado)) {
                estadoValido = true;
                break;
            }
        }

        if (!estadoValido) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado inválido. Estados permitidos: CONFIRMADO, PREPARANDO, EN_TRANSITO, ENTREGADO, CANCELADO");
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public PedidoDetalleDTO getDetalle(String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Pedido con ID " + pedidoId + " no encontrado"));

        String metodoPago = pagoRepository.findFirstByPedidoId(pedidoId)
            .map(Pago::getMetodoPago)
            .orElse("NO_REGISTRADO");

        var detalles = detallePedidoRepository.findByPedido_PedidoId(pedidoId);

        var items = detalles.stream().map(det -> {
            ItemDetalleDTO it = new ItemDetalleDTO();
            it.setProductoId(det.getProductoId());
            it.setCantidad(det.getCantidad());
            it.setPrecioUnitario(det.getPrecioUnitario());
            it.setSubtotal(det.getSubtotal());
            try {
                var prod = productoClient.getProductoById(det.getProductoId());
                String nombre = String.valueOf(prod.getOrDefault("nombre", prod.getOrDefault("name", "Producto")));
                it.setNombreProducto(nombre);
            } catch (Exception e) {
                it.setNombreProducto("Producto");
            }
            return it;
        }).toList();

        PedidoDetalleDTO dto = new PedidoDetalleDTO();
        dto.setPedidoId(pedido.getPedidoId());
        dto.setUsuarioId(pedido.getUsuarioId());
        dto.setDireccionId(pedido.getDireccionId());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());
        dto.setFecha(pedido.getFecha());
        dto.setMetodoPago(metodoPago);
        dto.setItems(items);

        return dto;
    }
}
