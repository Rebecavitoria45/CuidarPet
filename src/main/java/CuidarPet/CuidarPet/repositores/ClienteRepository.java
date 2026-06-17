package CuidarPet.CuidarPet.repositores;

import CuidarPet.CuidarPet.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    // Verificações para edição (se o dado já existe em OUTRO ID)
    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
