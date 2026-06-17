package CuidarPet.CuidarPet.dtos;

import CuidarPet.CuidarPet.models.Cliente;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String cpf,
        String nome,
        String telefone,
        String email,
        String logradouro,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String complemento,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    // Construtor para facilitar a conversão no Service
    public ClienteResponseDTO(Cliente c) {
        this(
                c.getId(),
                c.getCpf(),
                c.getNome(),
                c.getTelefone(),
                c.getEmail(),
                c.getEndereco() != null ? c.getEndereco().getLogradouro() : "",
                c.getEndereco() != null ? c.getEndereco().getNumero() : "",
                c.getEndereco() != null ? c.getEndereco().getBairro() : "",
                c.getEndereco() != null ? c.getEndereco().getCidade() : "",
                c.getEndereco() != null ? c.getEndereco().getEstado() : "",
                c.getEndereco() != null ? c.getEndereco().getComplemento() : "",
                c.getDataCriacao(),
                c.getDataAtualizacao()
        );
}}
