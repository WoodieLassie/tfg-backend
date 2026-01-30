package es.judith.bo;

import java.security.Key;

public interface JwtBO {
    String generateToken(String username, String role);
    Key getEncryptionKey();
}
