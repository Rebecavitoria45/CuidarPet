package CuidarPet.CuidarPet.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequestDTO(
        @NotNull LocalDate data,
        @NotNull LocalTime horario,
        @NotNull Long veterinarioId,
        @NotNull Long petId
) {
}
