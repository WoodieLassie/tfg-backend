package es.judith.security.domain;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "oauth2_authorization")
@Data
public class Authorization {

  @Id
  @Column(name = "id", nullable = false, length = 100)
  private String id;

  @Column(name = "registered_client_id", nullable = false, length = 100)
  private String registeredClientId;

  @Column(name = "principal_name", nullable = false, length = 200)
  private String principalName;

  @Column(name = "authorization_grant_type", nullable = false, length = 100)
  private String authorizationGrantType;

  @Column(name = "authorizedScopes", nullable = true, length = 1000)
  private String authorizedScopes;

  @Column(name = "attributes", nullable = true, length = 4000)
  private String attributes;

  @Column(name = "state", nullable = true, length = 500)
  private String state;

  @Column(name = "authorization_code_value", nullable = true, length = 4000)
  private String authorizationCodeValue;

  @Column(name = "authorization_code_issued_at", nullable = true)
  private Instant authorizationCodeIssuedAt;

  @Column(name = "authorization_code_expires_at", nullable = true)
  private Instant authorizationCodeExpiresAt;

  @Column(name = "authorization_code_metadata", nullable = true, length = 2000)
  private String authorizationCodeMetadata;

  @Column(name = "access_token_value", nullable = true, length = 4000)
  private String accessTokenValue;

  @Column(name = "access_token_issued_at", nullable = true)
  private Instant accessTokenIssuedAt;

  @Column(name = "access_token_expires_at", nullable = true)
  private Instant accessTokenExpiresAt;

  @Column(name = "access_token_metadata", nullable = true, length = 2000)
  private String accessTokenMetadata;

  @Column(name = "access_token_type", nullable = true, length = 100)
  private String accessTokenType;

  @Column(name = "access_token_scopes", nullable = true, length = 1000)
  private String accessTokenScopes;

  @Column(name = "oidc_id_token_value", nullable = true, length = 4000)
  private String oidcIdTokenValue;

  @Column(name = "oidc_id_token_issued_at", nullable = true)
  private Instant oidcIdTokenIssuedAt;

  @Column(name = "oidc_id_token_expires_at", nullable = true)
  private Instant oidcIdTokenExpiresAt;

  @Column(name = "oidc_id_token_metadata", nullable = true, length = 2000)
  private String oidcIdTokenMetadata;

  @Column(name = "refresh_token_value", nullable = true, length = 4000)
  private String refreshTokenValue;

  @Column(name = "refresh_token_issued_at", nullable = true)
  private Instant refreshTokenIssuedAt;

  @Column(name = "refresh_token_expires_at", nullable = true)
  private Instant refreshTokenExpiresAt;

  @Column(name = "refresh_token_metadata", nullable = true, length = 2000)
  private String refreshTokenMetadata;

  @Column(name = "user_code_value", nullable = true, length = 4000)
  private String userCodeValue;

  @Column(name = "user_code_issued_at", nullable = true)
  private Instant userCodeIssuedAt;

  @Column(name = "user_code_expires_at", nullable = true)
  private Instant userCodeExpiresAt;

  @Column(name = "user_code_metadata", nullable = true, length = 2000)
  private String userCodeMetadata;

  @Column(name = "device_code_value", nullable = true, length = 4000)
  private String deviceCodeValue;

  @Column(name = "device_code_issued_at", nullable = true)
  private Instant deviceCodeIssuedAt;

  @Column(name = "device_code_expires_at", nullable = true)
  private Instant deviceCodeExpiresAt;

  @Column(name = "device_code_metadata", nullable = true, length = 2000)
  private String deviceCodeMetadata;
}
