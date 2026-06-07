package CuidarPet.CuidarPet.config;

import CuidarPet.CuidarPet.models.Usuario;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class TokenProvider {

    @Value("${api.security.token.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    // Gerar um Token
    public String gerarToken(Usuario usuario) {
        try {
            return Jwts.builder()
                    .subject(usuario.getUsername())
                    .issuedAt(new Date())
                    .claim("role", usuario.getRole().name())
                    .claim("id", usuario.getId())
                    .expiration(new Date(System.currentTimeMillis() + 7200000)) // 2 horas de validade
                    .signWith(getSigningKey())
                    .compact();
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    // Validar o Token
    public Boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //metodo para extrair a matricula do token
    public String getMatriculaUsuario(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token){
     //validar assinatura e exíração do token
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }




}
