package CuidarPet.CuidarPet.dtos;

import CuidarPet.CuidarPet.models.Agendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponseDTO(
        Long id,
        LocalDate data,
        LocalTime horario,
        String veterinarioNome,
        String petNome,
        String status,
        Long petId,
        Long veterinarioId
        ) {
    public AgendamentoResponseDTO(Agendamento a) {
        this(a.getId(), a.getData(), a.getHorario(),
                a.getVeterinario().getNome(), a.getPet().getNome(), a.getStatus(),a.getVeterinario().getId(),a.getPet().getId());
}}
