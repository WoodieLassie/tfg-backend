package es.judith.security;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

public class CustomPasswordAuthenticationProvider implements AuthenticationProvider {

  private static final String ERROR_URI =
      "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
  private final OAuth2AuthorizationService authorizationService;
  private final UserDetailsService userDetailsService;
  private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
  private final PasswordEncoder passwordEncoder;

  public CustomPasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
      UserDetailsService userDetailsService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, PasswordEncoder passwordEncoder) {
    Assert.notNull(authorizationService, "authorizationService cannot be null");
    Assert.notNull(tokenGenerator, "TokenGenerator cannot be null");
    Assert.notNull(userDetailsService, "UserDetailsService cannot be null");
    this.authorizationService = authorizationService;
    this.userDetailsService = userDetailsService;
    this.tokenGenerator = tokenGenerator;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final CustomPasswordAuthenticationToken customPasswordAuthenticationToken =
        (CustomPasswordAuthenticationToken) authentication;
    final OAuth2ClientAuthenticationToken clientPrincipal =
        getAuthenticatedClientElseThrowInvalidClient(customPasswordAuthenticationToken);
    final RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
    final String username = customPasswordAuthenticationToken.getUsername();
    final String password = customPasswordAuthenticationToken.getPassword();
    final User user = getUserByUsername(username);

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.ACCESS_DENIED);
    }

    final OAuth2ClientAuthenticationToken oauth2ClientAuthenticationToken =
        (OAuth2ClientAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    oauth2ClientAuthenticationToken.setDetails(user);
    final SecurityContext newContext = SecurityContextHolder.createEmptyContext();
    newContext.setAuthentication(oauth2ClientAuthenticationToken);
    SecurityContextHolder.setContext(newContext);

    final Set<String> authorizedScopes = user.getAuthorities().stream()
        .map(scope -> scope.getAuthority())
        .filter(scope -> registeredClient.getScopes().contains(scope)).collect(Collectors.toSet());

    final DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext
        .builder().registeredClient(registeredClient).principal(clientPrincipal)
        .authorizationServerContext(AuthorizationServerContextHolder.getContext())
        .authorizedScopes(authorizedScopes)
        .authorizationGrantType(
            CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD)
        .authorizationGrant(customPasswordAuthenticationToken);

    final OAuth2Authorization.Builder authorizationBuilder =
        OAuth2Authorization.withRegisteredClient(registeredClient)
            .attribute(Principal.class.getName(), clientPrincipal).principalName(username)
            .authorizationGrantType(
                CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD)
            .authorizedScopes(authorizedScopes);

    OAuth2TokenContext tokenContext =
        tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
    final OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
    if (generatedAccessToken == null) {
      OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
          "The token generator failed to generate the access token.", ERROR_URI);
      throw new OAuth2AuthenticationException(error);
    }

    final OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
        generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
        generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
    authorizationBuilder.accessToken(accessToken);

    OAuth2RefreshToken refreshToken = null;
    if (registeredClient != null
        && registeredClient.getAuthorizationGrantTypes()
            .contains(AuthorizationGrantType.REFRESH_TOKEN)
        && !ClientAuthenticationMethod.NONE
            .equals(clientPrincipal.getClientAuthenticationMethod())) {
      tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
      final OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
      if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
            "The token generator failed to generate the refresh token.", ERROR_URI);
        throw new OAuth2AuthenticationException(error);
      }
      refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
      authorizationBuilder.refreshToken(refreshToken);
    }

    final OAuth2Authorization authorization = authorizationBuilder.build();
    this.authorizationService.save(authorization);

    return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
        refreshToken);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return CustomPasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
      Authentication authentication) {
    OAuth2ClientAuthenticationToken clientPrincipal = null;
    if (OAuth2ClientAuthenticationToken.class
        .isAssignableFrom(authentication.getPrincipal().getClass())) {
      clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
    }
    if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
      return clientPrincipal;
    }
    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
  }

  private User getUserByUsername(String username) {
    User user;
    try {
      user = (User) this.userDetailsService.loadUserByUsername(username);
    } catch (UsernameNotFoundException e) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.ACCESS_DENIED);
    }

    return user;
  }

}
