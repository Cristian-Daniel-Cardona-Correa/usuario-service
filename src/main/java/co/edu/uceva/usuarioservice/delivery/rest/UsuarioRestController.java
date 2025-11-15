package co.edu.uceva.usuarioservice.delivery.rest;

import co.edu.uceva.usuarioservice.delivery.dto.LoginRequest;
import co.edu.uceva.usuarioservice.domain.exception.CredencialesInvalidasException;
import co.edu.uceva.usuarioservice.domain.exception.UsuarioNoEncontradoException;
import co.edu.uceva.usuarioservice.domain.exception.ValidationException;
import co.edu.uceva.usuarioservice.domain.model.Usuario;
import co.edu.uceva.usuarioservice.domain.service.IUsuarioService;
import co.edu.uceva.usuarioservice.domain.exception.NoHayUsuariosException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/usuario-service")
public class UsuarioRestController {

    private final IUsuarioService usuarioService;

    private static final String MENSAJE = "mensaje";
    private static final String USUARIO = "usuario";
    private static final String USUARIOS = "usuarios";

    public UsuarioRestController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CredencialesInvalidasException("El usuario con email " + loginRequest.getEmail() + " no fue encontrado"));

        if (!usuario.getPassword().equals(loginRequest.getPassword())) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "Login exitoso");
        response.put(USUARIO, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> getUsuarios() {
        List<Usuario> productos = usuarioService.findAll();
        if (productos.isEmpty()) {
            throw new NoHayUsuariosException();
        }
        Map<String, Object> response = new HashMap<>();
        response.put(USUARIOS, productos);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()){
            throw new ValidationException(result);
        }
        Map<String, Object> response = new HashMap<>();
        Usuario nuevoUsuario = usuarioService.save(usuario);
        response.put(MENSAJE, "Usuario creado exitosamente");
        response.put(USUARIO, nuevoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
        usuarioService.delete(usuario);
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "Usuario eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody Usuario usuarioDetails, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setPassword(usuarioDetails.getPassword());
        Usuario usuarioActualizado = usuarioService.update(usuario);
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "Usuario actualizado exitosamente");
        response.put(USUARIO, usuarioActualizado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "Usuario encontrado exitosamente");
        response.put(USUARIO, usuario);
        return ResponseEntity.ok(response);
    }
}
