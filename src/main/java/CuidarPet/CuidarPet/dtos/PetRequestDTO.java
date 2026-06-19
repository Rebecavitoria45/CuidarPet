package CuidarPet.CuidarPet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequestDTO(
    @NotBlank(message = " Campo Nome é obrigatório")
    String nome,

    @NotNull(message = " Campo Cliente ID é obrigatório")
    Long clienteId,

    @NotBlank(message = " Campo Peso é obrigatório")
    float peso,

    @NotBlank(message = "Campo sexo é obrigatório")
    char sexo,

    @NotBlank(message = " Campo Espécie é obrigatório")
    String especie,

    @NotBlank(message = " Campo Raça é obrigatório")
    String raca,

    @NotBlank(message = " Campo Idade é obrigatório")
    int idade
){}
