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
                .uri("/usuario/{usuarioId}", usuarioId) // tu CarritoController tiene /carrito/usuario/{usuarioId}
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
