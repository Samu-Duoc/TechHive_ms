package com.ms_contacto.controller;

import com.ms_contacto.model.Contacto;
import com.ms_contacto.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/contacto")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @PostMapping("/guardar")
    public Contacto guardarContacto(@RequestBody Contacto contacto) {
        return contactoService.guardarContacto(contacto);
    }

    @GetMapping("/listar")
    public List<Contacto> listar() {
        return contactoService.listar();
}

    @GetMapping("/{id}")
    public Contacto buscarPorId(@PathVariable Long id) {
        return contactoService.buscarPorId(id);
    }


    //SOLO ADMIN (la vista admin)
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        contactoService.eliminar(id);
    }
}