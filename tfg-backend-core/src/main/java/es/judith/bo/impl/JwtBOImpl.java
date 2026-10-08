package es.judith.bo.impl;

import es.judith.bo.JwtBO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.Serial;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
@Transactional
public class JwtBOImpl implements JwtBO {

    private final String secretKey;
    @Serial
    private static final long serialVersionUID = -5174332563623333281L;
    private static final Logger LOG = LoggerFactory.getLogger(JwtBOImpl.class);

    public JwtBOImpl() {
        try {
            LOG.debug("JwtBOImpl: JwtBOImpl constructor");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey generatedKey = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(generatedKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateToken(String username) {
        LOG.debug("JwtBOImpl: generateToken");
        return Jwts
                .builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 24)) //Expira en 24 horas
                .signWith(getEncryptionKey())
                .subject(username)
                .compact();
    }

    @Override
    public SecretKey getEncryptionKey() {
        LOG.debug("JwtBOImpl: getEncryptionKey");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        LOG.debug("JwtBOImpl: extractUsername");
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        LOG.debug("JwtBOImpl: extractClaim");
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        LOG.debug("JwtBOImpl: extractAllClaims");
        return Jwts.parser()
                .verifyWith(getEncryptionKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        LOG.debug("JwtBOImpl: validateToken");
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public boolean isTokenExpired(String token) {
        LOG.debug("JwtBOImpl: isTokenExpired");
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        LOG.debug("JwtBOImpl: extractExpiration");
        return extractClaim(token, Claims::getExpiration);
    }
}
