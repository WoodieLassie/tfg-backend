package es.judith.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.StringUtils;

public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

  private final OAuth2AuthorizationService oauth2AuthorizationService;

  public CustomOpaqueTokenIntrospector(OAuth2AuthorizationService oauth2AuthorizationService) {
    super();
    this.oauth2AuthorizationService = oauth2AuthorizationService;
  }

  @Override
  public OAuth2AuthenticatedPrincipal introspect(String token) {
    final OAuth2Authorization authorization =
        this.oauth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
    if (authorization == null || authorization.getAccessToken() == null
        || !authorization.getAccessToken().isActive() || authorization.getAccessToken().isExpired()
        || authorization.getAccessToken().isInvalidated()) {
	  throw new BadOpaqueTokenException(String.format("Invalid token %s", token));
    }
    final List<GrantedAuthority> grantedAuthorities = extractAuthorities(authorization);

    return new OAuth2IntrospectionAuthenticatedPrincipal(authorization.getAttributes(),
        grantedAuthorities);
  }

  private List<GrantedAuthority> extractAuthorities(final OAuth2Authorization authorization) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    if (CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD
        .equals(authorization.getAuthorizationGrantType())) {
      authorities = getUserAuthorities(authorization);
    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS
        .equals(authorization.getAuthorizationGrantType())) {
      authorities = getApplicationAuthorities(authorization);
    }

    return authorities;
  }

  @SuppressWarnings("unchecked")
  private List<GrantedAuthority> getUserAuthorities(final OAuth2Authorization authorization) {
    final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    final Map<String, Object> principal =
        authorization.getAttribute(CustomPasswordAuthenticationConstants.PRINCIPAL);
    if (principal != null) {
      final Map<String, Object> details =
          (Map<String, Object>) principal.get(CustomPasswordAuthenticationConstants.DETAILS);
      final List<Map<String, Object>> authorities = (List<Map<String, Object>>) details
          .get(CustomPasswordAuthenticationConstants.AUTHORITIES);
      final List<Map<String, Object>> authorityList =
          (List<Map<String, Object>>) authorities.get(1);
      for (Map<String, Object> authorityMap : authorityList) {
        final String authority =
            (String) authorityMap.get(CustomPasswordAuthenticationConstants.AUTHORITY);
        if (StringUtils.hasText(authority)) {
          final GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
          grantedAuthorities.add(grantedAuthority);
        }
      }
    }
    return grantedAuthorities;
  }

  private List<GrantedAuthority> getApplicationAuthorities(
      final OAuth2Authorization authorization) {
    final List<GrantedAuthority> authorities = new ArrayList<>();
    for (String scope : authorization.getAuthorizedScopes()) {
      final GrantedAuthority authority = new SimpleGrantedAuthority(scope);
      authorities.add(authority);
    }
    return authorities;
  }

}
