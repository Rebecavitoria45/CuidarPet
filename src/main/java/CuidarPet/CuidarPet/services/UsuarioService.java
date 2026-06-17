package CuidarPet.CuidarPet.services;

import CuidarPet.CuidarPet.dtos.UsuarioEdicaoAdminDTO;
import CuidarPet.CuidarPet.dtos.UsuarioEdicaoDTO;
import CuidarPet.CuidarPet.dtos.UsuarioRequestDTO;
import CuidarPet.CuidarPet.dtos.UsuarioResponseDTO;
import CuidarPet.CuidarPet.enums.Role;
import CuidarPet.CuidarPet.models.Usuario;
import CuidarPet.CuidarPet.repositores.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // cadastro de usuároos
    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(@Valid UsuarioRequestDTO dto) {
        if (dto.matricula() == null || !dto.matricula().matches("\\d+")) {
            throw new RuntimeException("A matrícula deve conter apenas números!");
        }

        if (repository.existsByMatricula(dto.matricula())) {
            throw new RuntimeException("Já existe um usuário cadastrado com esta matrícula!");
        }

        if (repository.existsByEmail(dto.email())) {
            throw new RuntimeException("Este e-mail já está em uso!");
        }
        validarSenha(dto.senha());
        validarCrmv(dto.role(), dto.crmv());

        Usuario usuario = new Usuario();
        usuario.setMatricula(dto.matricula());
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setRole(dto.role());
        usuario.setCrmv(dto.role() == Role.VETERINARIO ? dto.crmv() : null);
        usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
        usuario.setAtivo(true);
        usuario.setAdmin(dto.admin());

        Usuario usuarioSalvo = repository.save(usuario);
        return converterParaDTO(usuarioSalvo);
    }

    // Listar todos os usuarios
    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::converterParaDTO) // Transforma cada Usuario em UsuarioResponseDTO
                .toList();
    }

    // metodo para desativar/ativar usuario
    @Transactional
    public UsuarioResponseDTO alternarStatus(Long id) {
        // Buscando o usuário
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Inverte o status atual
        usuario.setAtivo(!usuario.isAtivo());
        return converterParaDTO(repository.save(usuario));
    }


    // metodo de edicao para que o admin do sistema possa atualizar informacoes cadastrais de usuarios
    @Transactional
    public UsuarioResponseDTO atualizarUsuarioPorAdmin(Long id, @Valid UsuarioEdicaoAdminDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (repository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new RuntimeException("Este e-mail já está sendo utilizado!");
        }

        validarCrmv(dto.role(), dto.crmv());

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setRole(dto.role());
        usuario.setCrmv(dto.role() == Role.VETERINARIO ? dto.crmv() : null);
        usuario.setAdmin(dto.admin());
        return converterParaDTO(repository.save(usuario));
    }

    // metodo de edicao para o proprio usuario
    @Transactional
    public UsuarioResponseDTO atualizarProprioPerfil(String matricula, @Valid UsuarioEdicaoDTO dto) {
        Usuario usuario = repository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (repository.existsByEmailAndIdNot(dto.email(), usuario.getId())) {
            throw new RuntimeException("E-mail indisponível.");
        }
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());

        // Se o usuário preencheu a senha, validaremos
        if (dto.senha() != null && !dto.senha().isBlank()) {
            validarSenha(dto.senha()); // Chama a validação de complexidade
            usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
        }
        return converterParaDTO(repository.save(usuario));
    }



    // conversão do usuario para o dto de response
    private UsuarioResponseDTO converterParaDTO(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getMatricula(), u.getEmail(), u.getNome(), u.getRole(), u.getCrmv(), u.isAtivo(), u.isAdmin());
    }

    // Validação do CRMV
    private void validarCrmv(Role role, String crmv) {
        if (role == Role.VETERINARIO && (crmv == null || crmv.isBlank())) {
            throw new RuntimeException("CRMV é obrigatório para veterinários.");
        } else if (role == Role.VETERINARIO) {
            String crmvRegex = "^[0-9]{3,7}[/-][A-Z]{2}$";

            if (!crmv.trim().toUpperCase().matches(crmvRegex)) {
                throw new RuntimeException("Formato de CRMV inválido! Use o padrão: 00000/UF (Ex: 12345/SP)");
            }

        }
    }

    private void validarSenha(String senha) {
        // Explicação do Regex:
        // ^                 - Início da string
        // (?=.*[0-9])       - Pelo menos um número
        // (?=.*[A-Z])       - Pelo menos uma letra maiúscula
        // (?=.*[@#$%^&+=!]) - Pelo menos um caractere especial
        // .{6,}             - No mínimo 6 caracteres
        // $                 - Fim da string
        String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{6,}$";

        if (senha == null || !senha.matches(regex)) {
            throw new RuntimeException("A senha deve ter no mínimo 6 caracteres, " +
                    "conter uma letra maiúscula, um número e um caractere especial.");
        }
    }
}
