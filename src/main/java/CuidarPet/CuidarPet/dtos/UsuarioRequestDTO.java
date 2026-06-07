package CuidarPet.CuidarPet.dtos;
import CuidarPet.CuidarPet.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

public record UsuarioRequestDTO(
      @NotBlank(message = "Matricula é obrigatório")  String matricula,
      @NotBlank (message = "O e-mail é obrigatório")  String email,
      @NotBlank (message = "Nome é obrigatório")   String nome,
      @NonNull String senha,
      @NonNull  Role role,
      String crmv
) {
}
