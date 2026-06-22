package CuidarPet.CuidarPet.repositores;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import CuidarPet.CuidarPet.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import CuidarPet.CuidarPet.models.Agendamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByPetId(Long petId);

    List<Agendamento> findByStatus(String status);

    // Verifica se já existe um agendamento para este veterinário nesta data e hora
    boolean existsByVeterinarioAndDataAndHorario(Usuario veterinario, LocalDate data, LocalTime horario);


    // Lista por ID do Veterinário
    List<Agendamento> findByVeterinarioId(Long veterinarioId);

    // Lista por Data
    List<Agendamento> findByData(LocalDate data);

    // Lista por Veterinário e Data (Agenda do dia do médico)
    List<Agendamento> findByVeterinarioIdAndData(Long veterinarioId, LocalDate data);

    @Query("SELECT a.horario FROM Agendamento a WHERE a.veterinario.id = :vetId AND a.data = :data AND a.status <> 'CANCELADO'")
    List<LocalTime> findHorariosOcupados(@Param("vetId") Long vetId, @Param("data") LocalDate data);
}
