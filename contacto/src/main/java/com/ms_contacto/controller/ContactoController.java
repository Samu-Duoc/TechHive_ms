package com.ms_contacto.controller;

import com.ms_contacto.dto.ContactoDTO;
import com.ms_contacto.dto.CrearContactoDTO;
import com.ms_contacto.service.ContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacto")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactoController {

    private final ContactoService contactoService;

    // Cliente (web) envía mensaje de contacto
    @PostMapping
    public ContactoDTO crear(@Valid @RequestBody CrearContactoDTO dto) {
        return contactoService.crearMensaje(dto);
    }

    // Cliente ve sus mensajes (opcional)
    @GetMapping("/usuario/{usuarioId}")
    public List<ContactoDTO> listarPorUsuario(@PathVariable String usuarioId) {
        return contactoService.listarPorUsuario(usuarioId);
    }

    // Admin ve todos los mensajes
    @GetMapping
    public List<ContactoDTO> listarTodos() {
        return contactoService.listarTodos();
    }

    // Admin cambia estado: pendiente / respondido
    @PatchMapping("/{contactoId}/estado")
    public ContactoDTO cambiarEstado(@PathVariable String contactoId,@RequestParam String estado) {
        return contactoService.cambiarEstado(contactoId, estado);
    }
}