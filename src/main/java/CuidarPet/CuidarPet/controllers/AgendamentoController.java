package CuidarPet.CuidarPet.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import CuidarPet.CuidarPet.models.Agendamento;
import CuidarPet.CuidarPet.models.Pet;
import CuidarPet.CuidarPet.repositores.AgendamentoRepository;
import CuidarPet.CuidarPet.repositores.PetRepository;

@RestController
@RequestMapping("/agendamentos")
@CrossOrigin("*")
public class AgendamentoController {

    private final AgendamentoRepository agendamentoRepository;
    private final PetRepository petRepository;

    public AgendamentoController(AgendamentoRepository agendamentoRepository, PetRepository petRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.petRepository = petRepository;
    }

    @GetMapping
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Agendamento buscarPorId(@PathVariable Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }

    @PostMapping("/pet/{petId}")
    public Agendamento cadastrar(@PathVariable Long petId, @RequestBody Agendamento agendamento) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));

        agendamento.setPet(pet);
        return agendamentoRepository.save(agendamento);
    }

    @PutMapping("/{id}")
    public Agendamento editar(@PathVariable Long id, @RequestBody Agendamento dados) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        agendamento.setData(dados.getData());
        agendamento.setHorario(dados.getHorario());
        agendamento.setVeterinario(dados.getVeterinario());
        agendamento.setStatus(dados.getStatus());

        return agendamentoRepository.save(agendamento);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        agendamentoRepository.deleteById(id);
    }

    @GetMapping("/pet/{petId}")
    public List<Agendamento> listarPorPet(@PathVariable Long petId) {
        return agendamentoRepository.findByPetId(petId);
    }
}
