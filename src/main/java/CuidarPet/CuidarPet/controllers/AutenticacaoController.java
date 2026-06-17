package CuidarPet.CuidarPet.controllers;

import CuidarPet.CuidarPet.dtos.LoginRequestDTO;
import CuidarPet.CuidarPet.dtos.LoginResponseDTO;
import CuidarPet.CuidarPet.models.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import CuidarPet.CuidarPet.config.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dados) {

        // Cria um token interno do Spring com Matricula e Senha
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.matricula(), dados.senha());

        // O manager vai no seu UserDetailsServiceImpl buscar o usuario e comparar a senha
        // Se a senha estiver errada ou o usuário desativado, ele lança uma exceção aqui.
        var authentication = authenticationManager.authenticate(authenticationToken);

        // Se chegou aqui, a senha está correta. Pegamos o usuário autenticado.
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Geramos o Token JWT
        String tokenJWT = tokenProvider.gerarToken(usuario);

        // Retornamos o token e informações úteis para o Front-end (como nome e cargo)
        return ResponseEntity.ok(new LoginResponseDTO(
                tokenJWT,
                usuario.getNome(),
                usuario.getRole().name(),
                usuario.isAdmin()
        ));
    }
}
