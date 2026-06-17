package CuidarPet.CuidarPet.services;

import CuidarPet.CuidarPet.dtos.ClienteRequestDTO;
import CuidarPet.CuidarPet.dtos.ClienteResponseDTO;
import CuidarPet.CuidarPet.models.Cliente;
import CuidarPet.CuidarPet.models.Endereco;
import CuidarPet.CuidarPet.repositores.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    //cadastrar cliente
    @Transactional
    public ClienteResponseDTO cadastrarCliente(ClienteRequestDTO dto) {

            if (repository.existsByCpf(dto.cpf())) {
                throw new RuntimeException("Já existe um cliente cadastrado com este CPF!");
            }

            if (repository.existsByEmail(dto.email())) {
                throw new RuntimeException("Este e-mail já está em uso por outro cliente!");
            }


            Cliente cliente = new Cliente();
            cliente.setCpf(dto.cpf());
            cliente.setNome(dto.nome());
            cliente.setEmail(dto.email());
            String telefoneApenasNumeros = dto.telefone().replaceAll("[^0-9]", "");
            cliente.setTelefone(telefoneApenasNumeros);

            // Criando e setando o endereço
            Endereco endereco = new Endereco();
            endereco.setLogradouro(dto.logradouro());
            endereco.setNumero(dto.numero());
            endereco.setBairro(dto.bairro());
            endereco.setCidade(dto.cidade());
            endereco.setEstado(dto.estado());
            endereco.setComplemento(dto.complemento());

            cliente.setEndereco(endereco);

            Cliente clienteSalvo = repository.save(cliente);
            return converterParaDTO(clienteSalvo);


    }
     //editar cliente
    @Transactional
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO dto) {
            Cliente cliente = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

            if (repository.existsByEmailAndIdNot(dto.email(), id)) {
                throw new RuntimeException("Este e-mail já pertence a outro cliente!");
            }

            if (repository.existsByCpfAndIdNot(dto.cpf(), id)) {
                throw new RuntimeException("Este CPF já pertence a outro cliente!");
            }

            cliente.setCpf(dto.cpf());
            cliente.setNome(dto.nome());
            cliente.setEmail(dto.email());
            String telefoneApenasNumeros = dto.telefone().replaceAll("[^0-9]", "");
            cliente.setTelefone(telefoneApenasNumeros);

            Endereco endereco = cliente.getEndereco();
            if (endereco == null) endereco = new Endereco();

            endereco.setLogradouro(dto.logradouro());
            endereco.setNumero(dto.numero());
            endereco.setBairro(dto.bairro());
            endereco.setCidade(dto.cidade());
            endereco.setEstado(dto.estado());
            endereco.setComplemento(dto.complemento());

            cliente.setEndereco(endereco);

            return converterParaDTO(repository.save(cliente));

        }
    //excluir cliente
    @Transactional
    public void excluirCliente(Long id) {
            if (!repository.existsById(id)) {
                throw new RuntimeException("Cliente não encontrado para exclusão!");
            }
            repository.deleteById(id);

    }
    //listar clientes
    public List<ClienteResponseDTO> listarClientesTodos() {
        return repository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }
    //buscar cliente
    public ClienteResponseDTO buscarClientePorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
        return converterParaDTO(cliente);
    }

    private ClienteResponseDTO converterParaDTO(Cliente c) {
        return new ClienteResponseDTO(c);
    }
    }
