package com.ms_auth_usuarios.config;

import com.ms_auth_usuarios.model.Rol;
import com.ms_auth_usuarios.model.Usuario;
import com.ms_auth_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!usuarioRepository.existsByEmail("admin@techhive.cl")) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("Principal")
                    .rut("11111111-1")
                    .email("admin@techhive.cl")
                    .password(passwordEncoder.encode("Admin1234+"))
                    .telefono("999999999")
                    .direccion("Sede Mi casa ")
                    .rol(Rol.ADMIN)
                    .estado("Activo")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(admin);
        }

        if (!usuarioRepository.existsByEmail("vendedor@techhive.cl")) {
            Usuario vendedor = Usuario.builder()
                    .nombre("Vendedor")
                    .apellido("Tienda")
                    .rut("22222222-2")
                    .email("vendedor@techhive.cl")
                    .password(passwordEncoder.encode("Vendedor123*"))
                    .telefono("888888888")
                    .direccion("Sucursal La tienda online")
                    .rol(Rol.VENDEDOR)
                    .estado("Activo")
                    .fechaRegistro(LocalDateTime.now())
                    .build();
            usuarioRepository.save(vendedor);
        }
    }
}

