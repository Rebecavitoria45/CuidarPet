package CuidarPet.CuidarPet.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private LocalTime horario;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Usuario veterinario;

    private String status;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}
