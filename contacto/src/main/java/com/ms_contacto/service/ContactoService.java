package com.ms_contacto.service;

import com.ms_contacto.dto.ContactoDTO;
import com.ms_contacto.dto.CrearContactoDTO;
import com.ms_contacto.model.Contacto;
import com.ms_contacto.repository.ContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactoService {

    private final ContactoRepository contactoRepository;

    public ContactoDTO crearMensaje(CrearContactoDTO dto) {
        Contacto c = new Contacto();
        c.setContactoId(UUID.randomUUID().toString());
        c.setUsuarioId(dto.getUsuarioId());
        c.setAsunto(dto.getAsunto());
        c.setMensaje(dto.getMensaje());
        c.setFechaEnvio(LocalDateTime.now());
        c.setEstado("pendiente");

        c = contactoRepository.save(c);
        return toDTO(c);
    }

    public List<ContactoDTO> listarPorUsuario(String usuarioId) {
        return contactoRepository.findByUsuarioIdOrderByFechaEnvioDesc(usuarioId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ContactoDTO> listarTodos() {
        return contactoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ContactoDTO cambiarEstado(String contactoId, String nuevoEstado) {
        Contacto c = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        c.setEstado(nuevoEstado);
        c = contactoRepository.save(c);
        return toDTO(c);
    }

    private ContactoDTO toDTO(Contacto c) {
        ContactoDTO dto = new ContactoDTO();
        dto.setContactoId(c.getContactoId());
        dto.setUsuarioId(c.getUsuarioId());
        dto.setAsunto(c.getAsunto());
        dto.setMensaje(c.getMensaje());
        dto.setFechaEnvio(c.getFechaEnvio());
        dto.setEstado(c.getEstado());
        return dto;
    }
}
