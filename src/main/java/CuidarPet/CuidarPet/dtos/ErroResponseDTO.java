package CuidarPet.CuidarPet.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponseDTO(
        LocalDateTime timestamp,
        int status,
        String mensagem,
        List<String> detalhes
) {
}
