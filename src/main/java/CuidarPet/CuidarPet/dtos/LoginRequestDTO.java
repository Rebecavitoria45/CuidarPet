package CuidarPet.CuidarPet.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Matricula é obrigatório") String matricula,
        @NotBlank(message = "senha é obrigatório")  String senha
) { }
