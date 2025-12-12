package com.ms_pedidos_pagos.webclient;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProductoClient {

    private final WebClient webClient;
    
    //Evita duplicar propiedades y resuelve el placeholder que provocaba que Spring no pudiera crear el bean.
    public ProductoClient(@Value("${productos-categorias-service.url}") String productosServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(productosServiceUrl)
                .build();
    }

    public Map<String, Object> getProductoById(Long id) {
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public void descontarStock(Long id, Integer cantidad) {
        this.webClient.put()
                .uri("/{id}/stock/descontar?cantidad={cant}", id, cantidad)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}