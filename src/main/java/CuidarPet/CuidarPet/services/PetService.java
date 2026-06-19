package CuidarPet.CuidarPet.services;

import CuidarPet.CuidarPet.models.Cliente;
import CuidarPet.CuidarPet.dtos.PetResponseDTO;
import CuidarPet.CuidarPet.models.Pet;
import CuidarPet.CuidarPet.dtos.PetRequestDTO;
import CuidarPet.CuidarPet.repositores.ClienteRepository;
import CuidarPet.CuidarPet.repositores.PetRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final ClienteRepository ClienteRepository;

    //Cadastrar Pet, verificando se o pet está associado ao cliente
    @Transactional
    public PetResponseDTO cadastrarPet(PetRequestDTO dto) {
        Cliente cliente = ClienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID fornecido."));

        Pet pet = new Pet();
        pet.setNome(dto.nome());
        pet.setPeso(dto.peso());
        pet.setSexo(dto.sexo());
        pet.setEspecie(dto.especie());
        pet.setRaca(dto.raca());

        pet.setCliente(cliente);

        Pet petSalvo = petRepository.save(pet);
        return converterParaDTO(petSalvo);
    }
    //Atualizar dados do pet
     @Transactional
    public PetResponseDTO atualizarPet(Long id, PetRequestDTO dto) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado!"));

        pet.setNome(dto.nome());
        pet.setPeso(dto.peso());
        pet.setSexo(dto.sexo());
        pet.setEspecie(dto.especie());
        pet.setRaca(dto.raca());

        return converterParaDTO(petRepository.save(pet));
    }
    //Excluir pet
    @Transactional
    public void excluirPet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Pet não encontrado para exclusão!");
        }
        petRepository.deleteById(id);
    }
    //Listar Pet
    public List<PetResponseDTO> listarPetTodos() {
        return petRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }
    //Buscar pet
    public PetResponseDTO buscarPetPorId(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado!"));
        return converterParaDTO(pet);
    }
    private PetResponseDTO converterParaDTO(Pet p) {return new PetResponseDTO(p);
    }
}
