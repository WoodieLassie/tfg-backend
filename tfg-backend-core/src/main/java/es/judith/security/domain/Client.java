package es.judith.security.domain;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "oauth2_registered_client")
@Data
public class Client {

  @Id
  @Column(name = "id", nullable = false, length = 100)
  private String id;

  @Column(name = "client_id", nullable = false, length = 100)
  private String clientId;

  @Column(name = "client_id_issued_at", nullable = false)
  private Instant clientIdIssuedAt;

  @Column(name = "client_secret", nullable = true, length = 200)
  private String clientSecret;

  @Column(name = "client_secret_expires_at", nullable = true)
  private Instant clientSecretExpiresAt;

  @Column(name = "client_name", nullable = false, length = 200)
  private String clientName;

  @Column(name = "client_authentication_methods", nullable = false, length = 1000)
  private String clientAuthenticationMethods;

  @Column(name = "authorization_grant_types", nullable = false, length = 1000)
  private String authorizationGrantTypes;

  @Column(name = "redirect_uris", nullable = true, length = 1000)
  private String redirectUris;

  @Column(name = "post_logout_redirect_uris", nullable = true, length = 1000)
  private String postLogoutRedirectUris;

  @Column(name = "scopes", nullable = false, length = 1000)
  private String scopes;

  @Column(name = "client_settings", nullable = false, length = 2000)
  private String clientSettings;

  @Column(name = "token_settings", nullable = false, length = 2000)
  private String tokenSettings;
}
