package CuidarPet.CuidarPet.config;

import CuidarPet.CuidarPet.enums.Role;
import CuidarPet.CuidarPet.models.Usuario;
import CuidarPet.CuidarPet.repositores.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdministradorPadrão implements CommandLineRunner {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // Lendo os valores do application.properties
    @Value("${app.admin.matricula}")
    private String adminMatricula;

    @Value("${app.admin.nome}")
    private String adminNome;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) throws Exception {

        // Verifica se já existe um usuário com essa matrícula
        if (repository.findByMatricula(adminMatricula).isEmpty()) {

            System.out.println("Iniciando criação do usuário administrador padrão...");

            Usuario admin = new Usuario();
            admin.setMatricula(adminMatricula);
            admin.setNome(adminNome);
            admin.setEmail(adminEmail);
            admin.setRole(Role.ADMIN);
            admin.setAtivo(true);

            // Criptografa a senha vinda do properties
            admin.setSenhaHash(passwordEncoder.encode(adminSenha));

            repository.save(admin);

            System.out.println("Usuário administrador criado com sucesso!");
        } else {
            System.out.println("Usuário administrador já existe no sistema.");
        }
    }

}
