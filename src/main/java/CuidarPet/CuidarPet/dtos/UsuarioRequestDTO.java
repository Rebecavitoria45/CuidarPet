package CuidarPet.CuidarPet.dtos;
import CuidarPet.CuidarPet.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

public record UsuarioRequestDTO(
      @NotBlank(message = "Matricula é obrigatório")
      @Pattern(regexp = "\\d+", message = "A matrícula deve conter apenas números") String matricula,
      @NotBlank (message = "O e-mail é obrigatório")  String email,
      @NotBlank (message = "Nome é obrigatório")   String nome,
      @NonNull String senha,
      @NonNull  Role role,
      String crmv,
      Boolean admin

) {
}
