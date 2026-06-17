package CuidarPet.CuidarPet.config;

import CuidarPet.CuidarPet.dtos.ErroResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Captura erros de Negócio
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDTO> handleRuntimeException(RuntimeException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        String cause = ex.getMostSpecificCause().getMessage();
        String mensagem = "Erro de integridade nos dados enviados.";

        if (cause.contains("Duplicate entry")) {
            mensagem = "Erro: Já existe um registro com esses dados (E-mail ou CPF duplicado).";
        } else if (cause.contains("Data too long")) {
            mensagem = "Erro: Um dos campos informados é longo demais.";
        }

        return buildErrorResponse(HttpStatus.CONFLICT, mensagem, null);
    }

    //Captura erros de Login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponseDTO> handleBadCredentials() {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Matrícula ou senha inválidos",
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    //Captura erro de Usuário Desativado
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErroResponseDTO> handleDisabled() {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Este usuário está desativado no sistema",
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    // Captura erros de Validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação",
                detalhes
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Captura erros genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGeneralException(Exception ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado no servidor: " + ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    private ResponseEntity<ErroResponseDTO> buildErrorResponse(HttpStatus status, String mensagem, List<String> detalhes) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                status.value(),
                mensagem,
                detalhes
        );
        return ResponseEntity.status(status).body(erro);
    }
}
