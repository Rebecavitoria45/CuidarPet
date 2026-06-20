package CuidarPet.CuidarPet.controllers;

import java.time.LocalDate;
import java.util.List;

import CuidarPet.CuidarPet.dtos.AgendamentoEdicaoDTO;
import CuidarPet.CuidarPet.dtos.AgendamentoEdicaoStatusDTO;
import CuidarPet.CuidarPet.dtos.AgendamentoRequestDTO;
import CuidarPet.CuidarPet.dtos.AgendamentoResponseDTO;
import CuidarPet.CuidarPet.services.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import CuidarPet.CuidarPet.models.Agendamento;
import CuidarPet.CuidarPet.models.Pet;
import CuidarPet.CuidarPet.repositores.AgendamentoRepository;
import CuidarPet.CuidarPet.repositores.PetRepository;

@RestController
@RequestMapping("/agendamentos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> agendar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        return ResponseEntity.ok(service.criarAgendamento(dto));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> editar(@PathVariable Long id, @RequestBody @Valid AgendamentoEdicaoDTO dto) {
        return ResponseEntity.ok(service.atualizarAgendamento(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AgendamentoResponseDTO> mudarStatus(@PathVariable Long id, @RequestBody @Valid AgendamentoEdicaoStatusDTO dto) {
        return ResponseEntity.ok(service.alterarStatus(id, dto.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluirAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/veterinario/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorVeterinario(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPorVeterinario(id));
    }

    @GetMapping("/pet/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorPet(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPorPet(id));
    }

    @GetMapping("/data")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorData(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.listarPorData(data));
    }

    // Exemplo de URL: /agendamentos/agenda?vetId=1&data=2026-06-20
    @GetMapping("/agenda")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAgendaDoDia(
            @RequestParam Long vetId,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.listarAgendaDoDia(vetId, data));

    }
}
