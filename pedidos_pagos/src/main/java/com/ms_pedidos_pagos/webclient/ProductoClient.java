package com.ms_pedidos_pagos.webclient;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProductoClient {

        private final WebClient webClient;

        public ProductoClient(@Value("${productos-categorias-service.url}") String productosServiceUrl) {
                // Aumentar límite de buffer a 5 MB para respuestas grandes
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(productosServiceUrl)
                .exchangeStrategies(strategies)
                .build();
        }

        public Map<String, Object> getProductoById(Long id) {
                return this.webClient.get()
                        .uri("/{id}", id)
                        .retrieve()
                        .onStatus(status -> status.is4xxClientError(), resp ->
                                resp.bodyToMono(String.class).map(msg ->
                                        new RuntimeException("Producto no encontrado (ID=" + id + "). Detalle: " + msg)
                                )
                        )
                        .onStatus(status -> status.is5xxServerError(), resp ->
                                resp.bodyToMono(String.class).map(msg ->
                                        new RuntimeException("Error en ms_productos_categorias al obtener producto. Detalle: " + msg)
                                )
                        )
                        .bodyToMono(Map.class)
                        .block();
        }

        private Integer extraerEntero(Object value) {
                if (value == null) return null;
                if (value instanceof Integer) return (Integer) value;
                if (value instanceof Long) return ((Long) value).intValue();
                if (value instanceof Double) return ((Double) value).intValue();
                if (value instanceof Float) return ((Float) value).intValue();
                if (value instanceof String) {
                        try { return Integer.parseInt((String) value); }
                        catch (Exception ignored) { return null; }
                }
                return null;
        }

        public void descontarStock(Long id, Integer cantidad) {
                Map<String, Object> producto = getProductoById(id);
                if (producto == null || producto.isEmpty()) {
                throw new RuntimeException("Producto vacío al consultar (ID=" + id + ")");
                }

                                // Leer stock de 'stock' o fallback 'cantidad'
                                Integer stockActual = extraerEntero(producto.get("stock"));
                                if (stockActual == null) {
                                        stockActual = extraerEntero(producto.get("cantidad"));
                                }

                                if (stockActual == null) {
                                throw new RuntimeException("El producto (ID=" + id + ") no trae campo 'stock' ni 'cantidad'");
                                }

                if (cantidad == null || cantidad <= 0) {
                throw new RuntimeException("Cantidad inválida para descontar stock: " + cantidad);
                }

                int nuevoStock = stockActual - cantidad;
                if (nuevoStock < 0) {
                throw new RuntimeException("Stock insuficiente para producto (ID=" + id + "). Stock=" + stockActual + ", solicitado=" + cantidad);
                }

        Map<String, Object> body = Map.of("stock", nuevoStock);

        this.webClient.put()
                .uri("/{id}/stock", id)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), resp ->
                        resp.bodyToMono(String.class).map(msg ->
                                new RuntimeException("Error 4xx desde ms_productos: " + msg)
                        )
                )
                .onStatus(status -> status.is5xxServerError(), resp ->
                        resp.bodyToMono(String.class).map(msg ->
                                new RuntimeException("Error 5xx desde ms_productos: " + msg)
                        )
                )
                .bodyToMono(Map.class)
                .block();
        }
}
