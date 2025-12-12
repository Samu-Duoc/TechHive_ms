package com.ms_pedidos_pagos.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class CarritoClient {

    private final WebClient webClient;

    public CarritoClient(@Value("${carrito-service.url}") String carritoServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(carritoServiceUrl)
                .build();
    }

    public Map<String, Object> getCarritoByUsuarioId(Long usuarioId) {
        return this.webClient.get()
                .uri("/usuario/{usuarioId}", usuarioId) 
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("No se encontró carrito para usuarioId=" + usuarioId + ". Detalle: " + msg))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("Error en ms_carrito al obtener carrito. Detalle: " + msg))
                )
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> crearCarrito(Long usuarioId) {
        return this.webClient.post()
                .uri("")
                .bodyValue(Map.of("usuarioId", usuarioId))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("Error 4xx al crear carrito usuarioId=" + usuarioId + ". Detalle: " + msg))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .map(msg -> new RuntimeException("Error 5xx al crear carrito usuarioId=" + usuarioId + ". Detalle: " + msg))
                )
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> getOrCreateCarritoByUsuarioId(Long usuarioId) {
        try {
            return getCarritoByUsuarioId(usuarioId);
        } catch (RuntimeException ex) {
            crearCarrito(usuarioId);
            return getCarritoByUsuarioId(usuarioId);
        }
    }
}
