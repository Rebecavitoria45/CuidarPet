package CuidarPet.CuidarPet.repositores;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import CuidarPet.CuidarPet.models.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByPetId(Long petId);

    List<Agendamento> findByStatus(String status);
}
