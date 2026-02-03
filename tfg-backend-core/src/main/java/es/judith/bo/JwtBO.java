package es.judith.bo;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

public interface JwtBO {
    String generateToken(String username);
    SecretKey getEncryptionKey();
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails userDetails);
    <T> T extractClaim(String token, Function<Claims, T> claimResolver);
    Claims extractAllClaims(String token);
    boolean isTokenExpired(String token);
    Date extractExpiration(String token);
}
