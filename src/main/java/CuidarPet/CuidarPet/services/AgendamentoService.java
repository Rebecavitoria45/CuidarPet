package CuidarPet.CuidarPet.services;

import CuidarPet.CuidarPet.dtos.AgendamentoEdicaoDTO;
import CuidarPet.CuidarPet.dtos.AgendamentoRequestDTO;
import CuidarPet.CuidarPet.dtos.AgendamentoResponseDTO;
import CuidarPet.CuidarPet.enums.Role;
import CuidarPet.CuidarPet.models.Agendamento;
import CuidarPet.CuidarPet.models.Pet;
import CuidarPet.CuidarPet.models.Usuario;
import CuidarPet.CuidarPet.repositores.AgendamentoRepository;
import CuidarPet.CuidarPet.repositores.PetRepository;
import CuidarPet.CuidarPet.repositores.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;


    @Transactional
    public AgendamentoResponseDTO criarAgendamento(AgendamentoRequestDTO dto) {
        validarDataEHorario(dto.data(), dto.horario());
        // Busca o Veterinário e valida se ele tem a Role correta
        Usuario veterinario = usuarioRepository.findById(dto.veterinarioId())
                .orElseThrow(() -> new RuntimeException("Veterinário não encontrado!"));

        if (veterinario.getRole() != Role.VETERINARIO) {
            throw new RuntimeException("O usuário selecionado não é um médico veterinário.");
        }

        if (!veterinario.isEnabled()) { // ou isEnabled() dependendo do seu UserDetails
            throw new RuntimeException("Não é permitido agendar consultas para um veterinário inativo no sistema.");
        }

        // Validação de Conflito de Horário
        boolean ocupado = agendamentoRepository.existsByVeterinarioAndDataAndHorario(
                veterinario, dto.data(), dto.horario()
        );

        if (ocupado) {
            throw new RuntimeException("Este veterinário já possui um agendamento para este dia e horário.");
        }

        //Busca o Pet
        Pet pet = petRepository.findById(dto.petId())
                .orElseThrow(() -> new RuntimeException("Pet não encontrado!"));

        // 4. Cria o Agendamento
        Agendamento agendamento = Agendamento.builder()
                .data(dto.data())
                .horario(dto.horario())
                .veterinario(veterinario)
                .pet(pet)
                .status("AGENDADO")
                .build();

        return new AgendamentoResponseDTO(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponseDTO atualizarAgendamento(Long id, AgendamentoEdicaoDTO dto) {

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        Usuario veterinario = usuarioRepository.findById(dto.veterinarioId())
                .orElseThrow(() -> new RuntimeException("Veterinário não encontrado."));


        if (!veterinario.isEnabled()) { // ou isEnabled() dependendo do seu UserDetails
            throw new RuntimeException("Não é permitido agendar consultas para um veterinário inativo no sistema.");
        }

        validarDataEHorario(dto.data(), dto.horario());


        // Validação de conflito: Verifica se existe outro agendamento (ID diferente) para o mesmo vet/data/hora
        boolean conflito = agendamentoRepository.findAll().stream()
                .anyMatch(a -> !a.getId().equals(id) &&
                        a.getVeterinario().getId().equals(dto.veterinarioId()) &&
                        a.getData().equals(dto.data()) &&
                        a.getHorario().equals(dto.horario()));

        if (conflito) {
            throw new RuntimeException("Este veterinário já possui outro agendamento neste horário.");
        }

        Pet pet = petRepository.findById(dto.petId())
                .orElseThrow(() -> new RuntimeException("Pet não encontrado."));

        agendamento.setData(dto.data());
        agendamento.setHorario(dto.horario());
        agendamento.setVeterinario(veterinario);
        agendamento.setPet(pet);

        return new AgendamentoResponseDTO(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponseDTO alterarStatus(Long id, String novoStatus) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        agendamento.setStatus(novoStatus.toUpperCase());
        return new AgendamentoResponseDTO(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public void excluirAgendamento(Long id) {
        if (!agendamentoRepository.existsById(id)) {
            throw new RuntimeException("Agendamento não encontrado.");
        }
        agendamentoRepository.deleteById(id);
    }

    public List<AgendamentoResponseDTO> listarTodos() {
        return agendamentoRepository.findAll().stream()
                .map(AgendamentoResponseDTO::new)
                .toList();

    }

    public List<AgendamentoResponseDTO> listarPorVeterinario(Long id) {
        return agendamentoRepository.findByVeterinarioId(id)
                .stream().map(AgendamentoResponseDTO::new).toList();
    }

    public List<AgendamentoResponseDTO> listarPorPet(Long id) {
        return agendamentoRepository.findByPetId(id)
                .stream().map(AgendamentoResponseDTO::new).toList();
    }

    public List<AgendamentoResponseDTO> listarPorData(LocalDate data) {
        return agendamentoRepository.findByData(data)
                .stream().map(AgendamentoResponseDTO::new).toList();
    }

    // Útil para o dashboard do veterinário
    public List<AgendamentoResponseDTO> listarAgendaDoDia(Long vetId, LocalDate data) {
        return agendamentoRepository.findByVeterinarioIdAndData(vetId, data)
                .stream().map(AgendamentoResponseDTO::new).toList();

    }
    public List<LocalTime> listarHorariosOcupados(Long vetId, LocalDate data) {
        try {
            return agendamentoRepository.findHorariosOcupados(vetId, data);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar horários ocupados: " + e.getMessage());
        }
    }

    @Transactional
    public AgendamentoResponseDTO buscarAgendamentoPorId(Long id) {
        // Busca no banco de dados
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + id));

        // Converte para o DTO seguro
        return new AgendamentoResponseDTO(agendamento);
    }

    private void validarDataEHorario(LocalDate data, LocalTime horario) {
        LocalDate hoje = LocalDate.now();
        LocalTime agora = LocalTime.now();

        // Verifica se a data é anterior a hoje
        if (data.isBefore(hoje)) {
            throw new RuntimeException("Não é possível realizar agendamentos em datas passadas.");
        }

        // Se a data for hoje, verifica se o horário já passou
        if (data.isEqual(hoje) && horario.isBefore(agora)) {
            throw new RuntimeException("O horário selecionado para hoje já passou.");
        }
    }
}
