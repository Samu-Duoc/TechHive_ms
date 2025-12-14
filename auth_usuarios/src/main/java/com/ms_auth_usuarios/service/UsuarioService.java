package com.ms_auth_usuarios.service;

import com.ms_auth_usuarios.dto.CambiarPasswordDTO;
import com.ms_auth_usuarios.dto.LoginRequestDTO;
import com.ms_auth_usuarios.dto.RecuperarClaveDTO;
import com.ms_auth_usuarios.dto.RecuperarClaveSeguraDTO;
import com.ms_auth_usuarios.dto.RegistroUsuarioDTO;
import com.ms_auth_usuarios.dto.SetSecurityQADTO;
import com.ms_auth_usuarios.dto.UpdateProfileDTO;
import com.ms_auth_usuarios.dto.UsuarioDTO;
import com.ms_auth_usuarios.model.Rol;
import com.ms_auth_usuarios.model.Usuario;
import com.ms_auth_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_SECURITY_QUESTION = "¿Cuál es el nombre de tu primera mascota?";

    // Limpiar RUT: recibe "22.350.485-6" o "22350485-6" y retorna "223504856" (solo dígitos)
    private String limpiarRut(String rut) {
        if (rut == null) return null;
        return rut.replace(".", "").replace("-", "").trim().toUpperCase();
    }

    // Registrar
    public UsuarioDTO registrar(RegistroUsuarioDTO dto) {

        String rutLimpio = limpiarRut(dto.getRut());

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        if (usuarioRepository.existsByRut(rutLimpio)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RUT ya está registrado");
        }

        String preguntaSeguridad = (dto.getPreguntaSeguridad() == null || dto.getPreguntaSeguridad().isBlank())
            ? DEFAULT_SECURITY_QUESTION
            : dto.getPreguntaSeguridad().trim();

        String respuestaSeguridad = dto.getRespuestaSeguridad();
        String respuestaSeguridadHash = null;
        if (respuestaSeguridad != null && !respuestaSeguridad.isBlank()) {
            String normalizada = respuestaSeguridad.trim().toLowerCase();
            respuestaSeguridadHash = passwordEncoder.encode(normalizada);
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .rut(rutLimpio)
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
            .preguntaSeguridad(preguntaSeguridad)
            .respuestaSeguridad(respuestaSeguridadHash)
                .rol(Rol.CLIENTE)
                .estado("Activo")
                .fechaRegistro(LocalDateTime.now())
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return toDTO(guardado);
    }

    // Login
    public UsuarioDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos"));

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");
        }

        return toDTO(usuario);
    }

    // Obtener por ID
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return toDTO(usuario);
    }

    // Listar todos
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Actualizar usuario completo (incluye password). Úsalo si realmente lo necesitas.
    public UsuarioDTO actualizarUsuario(Long id, RegistroUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        String rutLimpio = limpiarRut(dto.getRut());

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setRut(rutLimpio);
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());

        Usuario actualizado = usuarioRepository.save(usuario);
        return toDTO(actualizado);
    }

    // Actualizar perfil (SIN validar currentPassword, SIN tocar password)
    public UsuarioDTO actualizarPerfil(Long id, UpdateProfileDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        String rutLimpio = limpiarRut(dto.getRut());

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setRut(rutLimpio);
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());

        Usuario actualizado = usuarioRepository.save(usuario);
        return toDTO(actualizado);
    }

    // Cambiar contraseña (requiere password actual)
    public void cambiarPassword(Long id, CambiarPasswordDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña actual no es correcta");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        usuarioRepository.save(usuario);
    }

    // Recuperar contraseña por email (sin seguridad) — si ya no lo usas, puedes borrarlo
    public void actualizarPasswordPorEmail(RecuperarClaveDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ese email"));

        usuario.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        usuarioRepository.save(usuario);
    }

    // Configurar pregunta/respuesta (requiere password actual)
    public void configurarPreguntaSeguridad(Long id, SetSecurityQADTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        String pregunta = (dto.getPregunta() == null || dto.getPregunta().isBlank())
                ? DEFAULT_SECURITY_QUESTION
                : dto.getPregunta().trim();
        String respuestaNormalizada = dto.getRespuesta().trim().toLowerCase();

        usuario.setPreguntaSeguridad(pregunta);
        usuario.setRespuestaSeguridad(passwordEncoder.encode(respuestaNormalizada));
        usuarioRepository.save(usuario);
    }

    // Recuperar contraseña validando respuesta de seguridad
    public void actualizarPasswordPorEmailConRespuesta(RecuperarClaveSeguraDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ese email"));

        if (usuario.getRespuestaSeguridad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tiene pregunta de seguridad configurada");
        }

        String respuestaNormalizada = dto.getRespuesta().trim().toLowerCase();

        if (!passwordEncoder.matches(respuestaNormalizada, usuario.getRespuestaSeguridad())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Respuesta incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        usuarioRepository.save(usuario);
    }

    // Mapper a DTO
    private UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rut(formatRut(usuario.getRut()))
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .rol(usuario.getRol().name())
                .estado(usuario.getEstado())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }

    private String formatRut(String rut) {
        if (rut == null || rut.length() < 2) return rut;

        // Limpiar primero (remover puntos y guiones)
        rut = rut.replace(".", "").replace("-", "").trim();

        String dv = rut.substring(rut.length() - 1); // último carácter (dígito verificador)
        String body = rut.substring(0, rut.length() - 1); // el resto

        // Formatear con puntos cada 3 dígitos de derecha a izquierda
        StringBuilder formatted = new StringBuilder();
        for (int i = body.length() - 1; i >= 0; i--) {
            if ((body.length() - i) > 1 && (body.length() - i) % 3 == 1) {
                formatted.insert(0, ".");
            }
            formatted.insert(0, body.charAt(i));
        }

        return formatted.toString() + "-" + dv;
    }
}
