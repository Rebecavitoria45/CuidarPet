package CuidarPet.CuidarPet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente", nullable = false)
    private Cliente cliente;

    @Column (nullable = false)
    private String corresponsavel;

    @Column (nullable = false)
    private float peso;

    @Column (nullable = false)
    private char sexo;

    @Column (nullable = false)
    private  String especie;

    @Column (nullable = false)
    private String raca;

    @Column (nullable = false)
    private int idade;
}