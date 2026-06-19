package CuidarPet.CuidarPet.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record PetEdicaoDTO(
        @NotBlank(message = "O nome do pet é obrigatório")
        String nome,

        @NotBlank(message = "A espécie é obrigatória")
        String especie,

        @NotBlank(message = "A raça é obrigatória")
        String raca,

        @NotNull(message = "A idade é obrigatória")
        @PositiveOrZero(message = "A idade não pode ser negativa")
        Integer idade,

        @NotNull(message = "O peso é obrigatório")
        @Positive(message = "O peso deve ser um valor positivo")
        Float peso,

        @NotBlank(message = "O sexo é obrigatório")
        String sexo,

        @NotNull(message = "O tutor responsável é obrigatório")
        Long clienteId, // Necessário para manter ou alterar o vínculo com o tutor

        String corresponsavel // Opcional (pode ser nulo ou vazio)

){}
