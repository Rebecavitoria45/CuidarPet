package CuidarPet.CuidarPet.dtos;

import CuidarPet.CuidarPet.enums.Role;

public record UsuarioResponseDTO(
        Long id,
        String matricula,
        String email,
        String nome,
        Role role,
        String crmv,
        Boolean ativo,
        Boolean admin
) {
}
