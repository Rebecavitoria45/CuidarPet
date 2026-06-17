package CuidarPet.CuidarPet.dtos;

import CuidarPet.CuidarPet.enums.Role;
import org.antlr.v4.runtime.misc.NotNull;

public record UsuarioEdicaoAdminDTO(
        String nome,
        String email,
        Role role,
        String matricula,
        String crmv,
        Boolean admin
) {
}
