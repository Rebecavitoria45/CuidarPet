package CuidarPet.CuidarPet.dtos;

import jakarta.validation.constraints.NotBlank;

public record AgendamentoEdicaoStatusDTO(
        @NotBlank String status
) {
}
