package co.edu.uceva.usuarioservice.domain.service;

import co.edu.uceva.usuarioservice.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    Usuario save(Usuario usuario);
    void delete(Usuario usuario);
    Usuario update(Usuario usuario);
    Optional<Usuario> findById(Long id);
    List<Usuario> findAll();
    Optional<Usuario> findByEmail(String email);
}
