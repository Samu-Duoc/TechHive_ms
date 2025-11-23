package com.ms_contacto.service;


import com.ms_contacto.model.Contacto;
import com.ms_contacto.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.List;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    // Guardar
    public Contacto guardarContacto(Contacto contacto) {
        if (contacto.getNombre() == null || contacto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return contactoRepository.save(contacto);
    }

    // Listar todos
    public List<Contacto> listar() {
        return contactoRepository.findAll();
    }

    // Buscar por id
    public Contacto buscarPorId(Long id) {
        Optional<Contacto> resultado = contactoRepository.findById(id);
        return resultado.orElseThrow(
                () -> new RuntimeException("Contacto con ID " + id + " no encontrado")
        );
    }

    // Eliminar por id (Admin)
    public void eliminar(Long id) {
        if (!contactoRepository.existsById(id)) {
            throw new RuntimeException("Contacto con ID " + id + " no existe");
        }
        contactoRepository.deleteById(id);
    }
}