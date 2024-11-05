package es.alten.security;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
public class CustomPasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

  private static final long serialVersionUID = 8467027087556832876L;
  @Getter
  private final String username;
  @Getter
  private final String password;
  @Getter
  private final Set<String> scopes;

  public CustomPasswordAuthenticationToken(Authentication clientPrincipal,
      @Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
    super(CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD, clientPrincipal,
        additionalParameters);
    this.username = additionalParameters != null
        ? (String) additionalParameters.get(OAuth2ParameterNames.USERNAME)
        : null;
    this.password = additionalParameters != null
        ? (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD)
        : null;
    this.scopes = Collections.unmodifiableSet(scopes != null ? scopes : Collections.emptySet());
  }

}