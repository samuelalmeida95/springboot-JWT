package autenticacao.jwt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import autenticacao.jwt.DTO.CredenciaisDTO;
import autenticacao.jwt.DTO.TokenDTO;
import autenticacao.jwt.exceptions.SenhaInvalidaException;
import autenticacao.jwt.models.Usuario;
import autenticacao.jwt.security.JwtService;
import autenticacao.jwt.services.UsuarioService;
import autenticacao.jwt.services.impl.UsuarioServiceImpl;

@RestController
@RequestMapping(value="/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private  UsuarioServiceImpl usuarioServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
  

    @GetMapping(value="/buscar/{idUsuario}")
    public ResponseEntity<Usuario> findById(@PathVariable long idUsuario) {
        return ResponseEntity.ok().body(usuarioService.findById(idUsuario));
    }

    @GetMapping(value="/listar")
    public ResponseEntity<List<Usuario>> findAll(){
        return ResponseEntity.ok().body(usuarioService.findAll());
    }

    @PostMapping(value="/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario create(@RequestBody Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        
        return usuarioServiceImpl.salvar(usuario);
    }  
    
    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
                     usuarioServiceImpl.autenticar(usuario);

            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
