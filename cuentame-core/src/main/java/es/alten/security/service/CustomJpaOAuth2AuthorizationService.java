package es.alten.security.service;

import java.util.List;
import java.util.Map;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.security.dao.AuthorizationRepository;
import es.alten.security.domain.Authorization;

@Service
public class CustomJpaOAuth2AuthorizationService implements OAuth2AuthorizationService {

  private final AuthorizationRepository authorizationRepository;
  private final RegisteredClientRepository registeredClientRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public CustomJpaOAuth2AuthorizationService(AuthorizationRepository authorizationRepository,
      RegisteredClientRepository registeredClientRepository) {
    super();
    Assert.notNull(authorizationRepository, "authorization repository cannot be null");
    Assert.notNull(registeredClientRepository, "registered client repository cannot be null");
    this.authorizationRepository = authorizationRepository;
    this.registeredClientRepository = registeredClientRepository;

    ClassLoader classLoader = CustomJpaOAuth2AuthorizationService.class.getClassLoader();
    List<com.fasterxml.jackson.databind.Module> securityModules =
        SecurityJackson2Modules.getModules(classLoader);
    this.objectMapper.registerModules(securityModules);
    this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
  }

  @Override
  public void save(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "Authorization cannot be null");
    this.authorizationRepository.save(toEntity(authorization));
  }

  @Override
  public void remove(OAuth2Authorization authorization) {
    Assert.notNull(authorization, "Authorization cannot be null");
    this.authorizationRepository.deleteById(authorization.getId());
  }

  @Override
  public OAuth2Authorization findById(String id) {
    Assert.notNull(id, "Id cannot be null");
    return this.authorizationRepository.findById(id).map(this::toObject).orElse(null);
  }

  @Override
  public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
    Assert.hasText(token, "Token cannot be null");
    OAuth2Authorization authorization = null;

    if (tokenType == null) {
      authorization =
          this.authorizationRepository.findByAnyValue(token).map(this::toObject).orElse(null);
    } else if (OAuth2ParameterNames.STATE.equalsIgnoreCase(tokenType.getValue())) {
      authorization =
          this.authorizationRepository.findByState(token).map(this::toObject).orElse(null);
    } else if (OAuth2ParameterNames.CODE.equalsIgnoreCase(tokenType.getValue())) {
      authorization = this.authorizationRepository.findByAuthorizationCodeValue(token)
          .map(this::toObject).orElse(null);
    } else if (OAuth2ParameterNames.ACCESS_TOKEN.equalsIgnoreCase(tokenType.getValue())) {
      authorization = this.authorizationRepository.findByAccessTokenValue(token).map(this::toObject)
          .orElse(null);
    } else if (OAuth2ParameterNames.REFRESH_TOKEN.equalsIgnoreCase(tokenType.getValue())) {
      authorization = this.authorizationRepository.findByRefreshTokenValue(token)
          .map(this::toObject).orElse(null);
    }

    return authorization;
  }

  private Authorization toEntity(OAuth2Authorization oauth2Authorization) {
    final Authorization authorization = new Authorization();
    setCommonTokenValues(oauth2Authorization, authorization);
    setAccessTokenValues(oauth2Authorization, authorization);
    setAuthorizationCodeTokenValues(oauth2Authorization, authorization);
    setDeviceCodeTokenValues(oauth2Authorization, authorization);
    setOidcIdTokenValues(oauth2Authorization, authorization);
    setRefreshTokenValues(oauth2Authorization, authorization);
    setUserCodeTokenValues(oauth2Authorization, authorization);

    return authorization;
  }

  private void setCommonTokenValues(OAuth2Authorization oauth2Authorization,
      final Authorization authorization) {
    authorization.setAttributes(writeMap(oauth2Authorization.getAttributes()));
    authorization
        .setAuthorizationGrantType(oauth2Authorization.getAuthorizationGrantType().getValue());
    authorization.setAuthorizedScopes(
        StringUtils.collectionToCommaDelimitedString(oauth2Authorization.getAuthorizedScopes()));
    authorization.setId(oauth2Authorization.getId());
    authorization.setPrincipalName(oauth2Authorization.getPrincipalName());
    authorization.setRegisteredClientId(oauth2Authorization.getRegisteredClientId());
    authorization.setState(oauth2Authorization.getAttribute(OAuth2ParameterNames.STATE));
  }

  private void setAccessTokenValues(OAuth2Authorization oauth2Authorization,
      final Authorization authorization) {
    final OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
        oauth2Authorization.getToken(OAuth2AccessToken.class);
    if (accessToken != null && accessToken.getToken() != null) {
      authorization.setAccessTokenExpiresAt(accessToken.getToken().getExpiresAt());
      authorization.setAccessTokenIssuedAt(accessToken.getToken().getIssuedAt());
      authorization.setAccessTokenMetadata(writeMap(accessToken.getMetadata()));
      if (accessToken.getToken().getScopes() != null) {
        authorization.setAccessTokenScopes(
            StringUtils.collectionToCommaDelimitedString(accessToken.getToken().getScopes()));
      }
      authorization.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
      authorization.setAccessTokenValue(accessToken.getToken().getTokenValue());
    }
  }

  private void setAuthorizationCodeTokenValues(OAuth2Authorization oauth2Authorization,
      final Authorization authorization) {
    final OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCodeToken =
        oauth2Authorization.getToken(OAuth2AuthorizationCode.class);
    if (authorizationCodeToken != null && authorizationCodeToken.getToken() != null) {
      authorization.setAuthorizationCodeExpiresAt(authorizationCodeToken.getToken().getExpiresAt());
      authorization.setAuthorizationCodeIssuedAt(authorizationCodeToken.getToken().getIssuedAt());
      authorization.setAuthorizationCodeMetadata(writeMap(authorizationCodeToken.getMetadata()));
      authorization.setAuthorizationCodeValue(authorizationCodeToken.getToken().getTokenValue());
    }
  }

  private void setDeviceCodeTokenValues(OAuth2Authorization oauth2Authorization,
      final Authorization authorization) {
    final OAuth2Authorization.Token<OAuth2DeviceCode> deviceCodeToken =
        oauth2Authorization.getToken(OAuth2DeviceCode.class);
    if (deviceCodeToken != null && deviceCodeToken.getToken() != null) {
      authorization.setDeviceCodeExpiresAt(deviceCodeToken.getToken().getExpiresAt());
      authorization.setDeviceCodeIssuedAt(deviceCodeToken.getToken().getIssuedAt());
      authorization.setDeviceCodeMetadata(writeMap(deviceCodeToken.getMetadata()));
      authorization.setDeviceCodeValue(deviceCodeToken.getToken().getTokenValue());
    }
  }

  private void setOidcIdTokenValues(OAuth2Authorization oauth2Authorization,
      final Authorization authorization) {
    final OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
        oauth2Authorization.getToken(OidcIdToken.class);
    if (oidcIdToken != null && oidcIdToken.getToken() != null) {
      authorization.setOidcIdTokenExpiresAt(oidcIdToken.getToken().getExpiresAt());
      authorization.setOidcIdTokenIssuedAt(oidcIdToken.getToken().getIssuedAt());
      authorization.setOidcIdTokenMetadata(writeMap(oidcIdToken.getMetadata()));
      authorization.setOidcIdTokenValue(oidcIdToken.getToken().getTokenValue());
    }
  }

  private void setRefreshTokenValues(OAuth2Authorization oauth2Authorization,
      final Authorization authorization) {
    final OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
        oauth2Authorization.getToken(OAuth2RefreshToken.class);
    if (refreshToken != null && refreshToken.getToken() != null) {
      authorization.setRefreshTokenExpiresAt(refreshToken.getToken().getExpiresAt());
      authorization.setRefreshTokenIssuedAt(refreshToken.getToken().getIssuedAt());
      authorization.setRefreshTokenMetadata(writeMap(refreshToken.getMetadata()));
      authorization.setRefreshTokenValue(refreshToken.getToken().getTokenValue());
    }
  }

  private void setUserCodeTokenValues(OAuth2Authorization oauth2Authorization,
      final Authorization authorization) {
    final OAuth2Authorization.Token<OAuth2UserCode> userCodeToken =
        oauth2Authorization.getToken(OAuth2UserCode.class);
    if (userCodeToken != null && userCodeToken.getToken() != null) {
      authorization.setUserCodeExpiresAt(userCodeToken.getToken().getExpiresAt());
      authorization.setUserCodeIssuedAt(userCodeToken.getToken().getIssuedAt());
      authorization.setUserCodeMetadata(writeMap(userCodeToken.getMetadata()));
      authorization.setUserCodeValue(userCodeToken.getToken().getTokenValue());
    }
  }

  private OAuth2Authorization toObject(Authorization authorization) {
    final RegisteredClient registeredClient =
        this.registeredClientRepository.findById(authorization.getRegisteredClientId());
    if (registeredClient == null) {
      throw new DataRetrievalFailureException(String.format(
          "Registered client with id %s does not exist", authorization.getRegisteredClientId()));
    }

    final OAuth2Authorization.Builder builder =
        OAuth2Authorization.withRegisteredClient(registeredClient);
    setCommomTokenValues(authorization, builder);
    setAccessTokenValues(authorization, builder);
    setAuthorizationCodeTokenValues(authorization, builder);
    setDeviceCodeTokenValues(authorization, builder);
    setOidcIdTokenValues(authorization, builder);
    setRefreshTokenValues(authorization, builder);
    setUserCodeTokenValues(authorization, builder);

    return builder.build();
  }

  private void setCommomTokenValues(Authorization authorization,
      final OAuth2Authorization.Builder builder) {
    builder
        .attributes(attributes -> attributes.putAll(parseAttributes(authorization.getAttributes())))
        .authorizationGrantType(
            resolveAuthorizationGrantType(authorization.getAuthorizationGrantType()))
        .authorizedScopes(StringUtils.commaDelimitedListToSet(authorization.getAuthorizedScopes()))
        .id(authorization.getId()).principalName(authorization.getPrincipalName());
    if (StringUtils.hasText(authorization.getState())) {
      builder.attribute(OAuth2ParameterNames.STATE, authorization.getState());
    }
  }

  private void setAccessTokenValues(Authorization authorization,
      final OAuth2Authorization.Builder builder) {
    if (StringUtils.hasText(authorization.getAccessTokenValue())) {
      final OAuth2AccessToken accessToken = new OAuth2AccessToken(
          OAuth2AccessToken.TokenType.BEARER, authorization.getAccessTokenValue(),
          authorization.getAccessTokenIssuedAt(), authorization.getAccessTokenExpiresAt(),
          StringUtils.commaDelimitedListToSet(authorization.getAccessTokenScopes()));
      builder.token(accessToken,
          metadata -> metadata.putAll(parseMap(authorization.getAccessTokenMetadata())));
    }
  }

  private void setAuthorizationCodeTokenValues(Authorization authorization,
      final OAuth2Authorization.Builder builder) {
    if (StringUtils.hasText(authorization.getAuthorizationCodeValue())) {
      final OAuth2AuthorizationCode authorizationCodeToken = new OAuth2AuthorizationCode(
          authorization.getAuthorizationCodeValue(), authorization.getAuthorizationCodeIssuedAt(),
          authorization.getAuthorizationCodeExpiresAt());
      builder.token(authorizationCodeToken,
          metadata -> metadata.putAll(parseMap(authorization.getAuthorizationCodeMetadata())));
    }
  }

  private void setDeviceCodeTokenValues(Authorization authorization,
      final OAuth2Authorization.Builder builder) {
    if (StringUtils.hasText(authorization.getDeviceCodeValue())) {
      final OAuth2DeviceCode deviceCodeToken =
          new OAuth2DeviceCode(authorization.getDeviceCodeValue(),
              authorization.getDeviceCodeIssuedAt(), authorization.getDeviceCodeExpiresAt());
      builder.token(deviceCodeToken,
          metadata -> metadata.putAll(parseMap(authorization.getDeviceCodeMetadata())));
    }
  }

  private void setOidcIdTokenValues(Authorization authorization,
      final OAuth2Authorization.Builder builder) {
    if (StringUtils.hasText(authorization.getOidcIdTokenValue())) {
      final OidcIdToken oidcIdToken = new OidcIdToken(authorization.getOidcIdTokenValue(),
          authorization.getOidcIdTokenIssuedAt(), authorization.getOidcIdTokenExpiresAt(),
          parseMap(authorization.getOidcIdTokenMetadata()));
      builder.token(oidcIdToken,
          metadata -> metadata.putAll(parseMap(authorization.getOidcIdTokenMetadata())));
    }
  }

  private void setRefreshTokenValues(Authorization authorization,
      final OAuth2Authorization.Builder builder) {
    if (StringUtils.hasText(authorization.getRefreshTokenValue())) {
      final OAuth2RefreshToken refreshToken =
          new OAuth2RefreshToken(authorization.getRefreshTokenValue(),
              authorization.getRefreshTokenIssuedAt(), authorization.getRefreshTokenExpiresAt());
      builder.token(refreshToken,
          metadata -> metadata.putAll(parseMap(authorization.getRefreshTokenMetadata())));
    }
  }

  private void setUserCodeTokenValues(Authorization authorization,
      final OAuth2Authorization.Builder builder) {
    if (StringUtils.hasText(authorization.getUserCodeValue())) {
      final OAuth2UserCode userCodeToken = new OAuth2UserCode(authorization.getUserCodeValue(),
          authorization.getUserCodeIssuedAt(), authorization.getUserCodeExpiresAt());
      builder.token(userCodeToken,
          metadata -> metadata.putAll(parseMap(authorization.getUserCodeMetadata())));
    }
  }

  private Map<String, Object> parseMap(String data) {
    try {
      return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  private Map<String, Object> parseAttributes(String data) {
    try {
      final ObjectMapper attributesMapper = new ObjectMapper();
      return attributesMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  private String writeMap(Map<String, Object> data) {
    try {
      return this.objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  private static AuthorizationGrantType resolveAuthorizationGrantType(
      String authorizationGrantType) {
    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.AUTHORIZATION_CODE;
    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()
        .equals(authorizationGrantType)) {
      return AuthorizationGrantType.CLIENT_CREDENTIALS;
    } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
      return AuthorizationGrantType.REFRESH_TOKEN;
    }
    return new AuthorizationGrantType(authorizationGrantType); // Custom authorization grant type
  }
}
