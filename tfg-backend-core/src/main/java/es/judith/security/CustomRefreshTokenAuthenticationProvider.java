package es.judith.security;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

public class CustomRefreshTokenAuthenticationProvider implements AuthenticationProvider {
  private static final String ERROR_URI =
      "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
  private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE =
      new OAuth2TokenType(OidcParameterNames.ID_TOKEN);
  private final Log logger = LogFactory.getLog(getClass());
  private final OAuth2AuthorizationService authorizationService;
  private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
  private final UserDetailsService userDetailsService;

  public CustomRefreshTokenAuthenticationProvider(OAuth2AuthorizationService authorizationService,
      OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
      UserDetailsService userDetailsService) {
    Assert.notNull(authorizationService, "authorizationService cannot be null");
    Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
    this.authorizationService = authorizationService;
    this.tokenGenerator = tokenGenerator;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    OAuth2RefreshTokenAuthenticationToken refreshTokenAuthentication =
        (OAuth2RefreshTokenAuthenticationToken) authentication;

    OAuth2ClientAuthenticationToken clientPrincipal =
        getAuthenticatedClientElseThrowInvalidClient(refreshTokenAuthentication);
    RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

    if (this.logger.isTraceEnabled()) {
      this.logger.trace("Retrieved registered client");
    }

    OAuth2Authorization authorization = this.authorizationService
        .findByToken(refreshTokenAuthentication.getRefreshToken(), OAuth2TokenType.REFRESH_TOKEN);
    if (authorization == null) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
    }

    if (this.logger.isTraceEnabled()) {
      this.logger.trace("Retrieved authorization with refresh token");
    }

    if (!registeredClient.getId().equals(authorization.getRegisteredClientId())) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
    }

    if (!registeredClient.getAuthorizationGrantTypes()
        .contains(AuthorizationGrantType.REFRESH_TOKEN)) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
    }

    OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
    if (!refreshToken.isActive() || refreshToken.isExpired() || refreshToken.isInvalidated()) {
      // As per https://tools.ietf.org/html/rfc6749#section-5.2
      // invalid_grant: The provided authorization grant (e.g., authorization code,
      // resource owner credentials) or refresh token is invalid, expired, revoked [...].
      this.authorizationService.remove(authorization);
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
    }

    // As per https://tools.ietf.org/html/rfc6749#section-6
    // The requested scope MUST NOT include any scope not originally granted by the resource owner,
    // and if omitted is treated as equal to the scope originally granted by the resource owner.
    Set<String> scopes = refreshTokenAuthentication.getScopes();
    Set<String> authorizedScopes = authorization.getAuthorizedScopes();
    if (!authorizedScopes.containsAll(scopes)) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
    }

    if (this.logger.isTraceEnabled()) {
      this.logger.trace("Validated token request parameters");
    }

    if (scopes.isEmpty()) {
      scopes = authorizedScopes;
    }

    final UserDetails user =
        this.userDetailsService.loadUserByUsername(authorization.getPrincipalName());
    if (user == null) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
    }

    clientPrincipal.setDetails(user);

    final DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext
        .builder().registeredClient(registeredClient).principal(clientPrincipal)
        .authorizationServerContext(AuthorizationServerContextHolder.getContext())
        .authorizedScopes(scopes)
        .authorizationGrantType(
            CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD)
        .authorizationGrant(refreshTokenAuthentication);

    final OAuth2Authorization.Builder authorizationBuilder =
        OAuth2Authorization.withRegisteredClient(registeredClient)
            .attribute(Principal.class.getName(), clientPrincipal)
            .principalName(authorization.getPrincipalName())
            .authorizationGrantType(
                CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD)
            .authorizedScopes(authorizedScopes);

    // ----- Access token -----
    OAuth2TokenContext tokenContext =
        tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
    OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
    if (generatedAccessToken == null) {
      OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
          "The token generator failed to generate the access token.", ERROR_URI);
      throw new OAuth2AuthenticationException(error);
    }

    if (this.logger.isTraceEnabled()) {
      this.logger.trace("Generated access token");
    }

    OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
        generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
        generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
    if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
      authorizationBuilder.token(accessToken, metadata -> {
        metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
            claimAccessor.getClaims());
        metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, false);
      });
    } else {
      authorizationBuilder.accessToken(accessToken);
    }

    // ----- Refresh token -----
    OAuth2RefreshToken currentRefreshToken = refreshToken.getToken();
    if (!registeredClient.getTokenSettings().isReuseRefreshTokens() || refreshToken.isExpired()
        || !refreshToken.isActive() || refreshToken.isInvalidated()) {
      tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
      OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
      if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
            "The token generator failed to generate the refresh token.", ERROR_URI);
        throw new OAuth2AuthenticationException(error);
      }

      if (this.logger.isTraceEnabled()) {
        this.logger.trace("Generated refresh token");
      }

      currentRefreshToken = (OAuth2RefreshToken) generatedRefreshToken;
    }
    authorizationBuilder.refreshToken(currentRefreshToken);

    // ----- ID token -----
    OidcIdToken idToken;
    if (authorizedScopes.contains(OidcScopes.OPENID)) {
      // @formatter:off
          tokenContext = tokenContextBuilder
                  .tokenType(ID_TOKEN_TOKEN_TYPE)
                  .authorization(authorizationBuilder.build())    // ID token customizer may need access to the access token and/or refresh token
                  .build();
          // @formatter:on
      OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
      if (!(generatedIdToken instanceof Jwt)) {
        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
            "The token generator failed to generate the ID token.", ERROR_URI);
        throw new OAuth2AuthenticationException(error);
      }

      if (this.logger.isTraceEnabled()) {
        this.logger.trace("Generated id token");
      }

      idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
          generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
      authorizationBuilder.token(idToken, metadata -> metadata
          .put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
    } else {
      idToken = null;
    }

    this.authorizationService.remove(authorization);

    authorization = authorizationBuilder.build();

    this.authorizationService.save(authorization);

    if (this.logger.isTraceEnabled()) {
      this.logger.trace("Saved authorization");
    }

    Map<String, Object> additionalParameters = Collections.emptyMap();
    if (idToken != null) {
      additionalParameters = new HashMap<>();
      additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
    }

    if (this.logger.isTraceEnabled()) {
      this.logger.trace("Authenticated token request");
    }

    return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
        currentRefreshToken, additionalParameters);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return OAuth2RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
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

}
