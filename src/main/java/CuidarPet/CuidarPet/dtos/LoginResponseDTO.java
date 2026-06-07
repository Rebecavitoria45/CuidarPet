package CuidarPet.CuidarPet.dtos;

public record LoginResponseDTO(
        String token,
        String nome,
        String role
) {
}
