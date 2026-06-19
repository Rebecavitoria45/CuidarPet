package CuidarPet.CuidarPet.dtos;
import jakarta.validation.constraints.NotBlank;

public record PetEdicaoDTO(
        String nome,
        String peso,
        String idade
){}
