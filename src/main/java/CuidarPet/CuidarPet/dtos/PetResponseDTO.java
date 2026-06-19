package CuidarPet.CuidarPet.dtos;

import CuidarPet.CuidarPet.models.Pet;

public record PetResponseDTO(
        Long id,
        String nome,
        Long clienteId,
        String corresponsavel,
        float peso,
        String sexo,
        String especie,
        String raca,
        int idade,
        String tutorNome
){
    public PetResponseDTO(Pet pet){
       this(
        pet.getId(),
        pet.getNome(),
        pet.getCliente().getId(),
        pet.getCorresponsavel(),
        pet.getPeso(),
        pet.getSexo(),
        pet.getEspecie(),
        pet.getRaca(),
        pet.getIdade(),
               pet.getCliente().getNome()
       );
    }
}