package CuidarPet.CuidarPet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PetRequestDTO(
    @NotBlank(message = " Campo Nome é obrigatório")
    String nome,

    @NotNull(message = " Campo Cliente ID é obrigatório")
    Long clienteId,

    @NotNull(message = " Campo Peso é obrigatório")
    @PositiveOrZero(message = "o Peso não pode ser negativa")
    float peso,

    @NotBlank(message = "Campo sexo é obrigatório")
    String sexo,

    @NotBlank(message = " Campo Espécie é obrigatório")
    String especie,

    @NotBlank(message = " Campo Raça é obrigatório")
    String raca,

    @NotNull(message = " Campo Idade é obrigatório")
    @PositiveOrZero(message = "A idade não pode ser negativa")
    int idade,

    String corresponsavel
){}
