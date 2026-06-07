package CuidarPet.CuidarPet.dtos;

import jakarta.validation.constraints.NotBlank;


public record UsuarioEdicaoDTO(
       String nome,
       String email,
        String senha // Opcional
) {
}
