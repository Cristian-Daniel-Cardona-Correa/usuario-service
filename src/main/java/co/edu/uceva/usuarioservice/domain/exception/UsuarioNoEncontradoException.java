package co.edu.uceva.usuarioservice.domain.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(Long id) {
        super("El usuario con ID " + id + " no fue encontrado.");
    }
}
