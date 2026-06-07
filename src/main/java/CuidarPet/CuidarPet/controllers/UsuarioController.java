package CuidarPet.CuidarPet.controllers;

import CuidarPet.CuidarPet.dtos.UsuarioEdicaoAdminDTO;
import CuidarPet.CuidarPet.dtos.UsuarioEdicaoDTO;
import CuidarPet.CuidarPet.dtos.UsuarioRequestDTO;
import CuidarPet.CuidarPet.dtos.UsuarioResponseDTO;
import CuidarPet.CuidarPet.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor

public class UsuarioController {
    private final UsuarioService service;

    // cadastro de usuario
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO novoUsuario = service.cadastrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // Alternar status (Ativar/Desativar)
    @PatchMapping("/{id}/status")
    public ResponseEntity<UsuarioResponseDTO> alternarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(service.alternarStatus(id));
    }

    //  Edição administrativa
    @PutMapping("/{id}/admin")
    public ResponseEntity<UsuarioResponseDTO> editarPorAdmin(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioEdicaoAdminDTO dto) {
        return ResponseEntity.ok(service.atualizarUsuarioPorAdmin(id, dto));
    }

    // Edição do próprio perfil (Nome, Email, Senha)
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> editarProprioPerfil(
            @RequestBody @Valid UsuarioEdicaoDTO dto,
            Authentication authentication) {

        // O getName() aqui retorna o "Subject" que definimos no TokenProvider (a matrícula)
        String matriculaLogada = authentication.getName();
        return ResponseEntity.ok(service.atualizarProprioPerfil(matriculaLogada, dto));
    }

    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = service.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

}
