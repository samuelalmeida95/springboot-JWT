package autenticacao.jwt.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import autenticacao.jwt.models.Usuario;
import autenticacao.jwt.repositories.UsuarioRepository;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findById(Long idUsuario) {
        Optional<Usuario> usuarioBuscado = usuarioRepository.findById(idUsuario);
        return usuarioBuscado.get();
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

}
