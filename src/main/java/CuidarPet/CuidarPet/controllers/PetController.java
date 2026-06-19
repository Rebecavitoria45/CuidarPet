package CuidarPet.CuidarPet.controllers;


import CuidarPet.CuidarPet.dtos.PetEdicaoDTO;
import CuidarPet.CuidarPet.services.PetService;
import CuidarPet.CuidarPet.dtos.PetRequestDTO;
import CuidarPet.CuidarPet.dtos.PetResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor

public class PetController {
    private final PetService service;

    @PostMapping
    public ResponseEntity<PetResponseDTO> cadastrar(@RequestBody @Valid PetRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarPet(dto));
    }
    @GetMapping
    public ResponseEntity<List<PetResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarPetTodos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPetPorId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> editar(@PathVariable Long id, @RequestBody @Valid PetEdicaoDTO dto) {
        return ResponseEntity.ok(service.atualizarPet(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirPet(id);
        return ResponseEntity.noContent().build();
    }
}
