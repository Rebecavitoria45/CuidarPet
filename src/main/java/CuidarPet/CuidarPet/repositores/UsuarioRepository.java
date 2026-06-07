package CuidarPet.CuidarPet.repositores;

import CuidarPet.CuidarPet.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);

    // Verificação para edição
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByMatriculaAndIdNot(String matricula, Long id);

}
