package es.alten.security.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.alten.security.dao.ClientRepository;
import es.alten.security.domain.Client;

@Service
public class CustomJpaRegisteredClientRepository implements RegisteredClientRepository {

  private final ClientRepository clientRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public CustomJpaRegisteredClientRepository(ClientRepository clientRepository) {
    super();
    Assert.notNull(clientRepository, "client repository cannot be null");
    this.clientRepository = clientRepository;

    ClassLoader classLoader = CustomJpaRegisteredClientRepository.class.getClassLoader();
    List<com.fasterxml.jackson.databind.Module> securityModules =
        SecurityJackson2Modules.getModules(classLoader);
    this.objectMapper.registerModules(securityModules);
    this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
  }

  @Override
  public void save(RegisteredClient registeredClient) {
    Assert.notNull(registeredClient, "registered client cannot be null");
    this.clientRepository.save(toEntity(registeredClient));
  }

  @Override
  public RegisteredClient findById(String id) {
    Assert.hasText(id, "id cannot be empty");

    return this.clientRepository.findById(id).map(this::toObject).orElse(null);
  }

  @Override
  public RegisteredClient findByClientId(String clientId) {
    Assert.hasText(clientId, "client id cannot be empty");

    return this.clientRepository.findByClientId(clientId).map(this::toObject).orElse(null);
  }

  private Client toEntity(RegisteredClient registeredClient) {
    final Client client = new Client();
    final List<String> clientAuthenticationMethods =
        registeredClient.getClientAuthenticationMethods().stream()
            .map(ClientAuthenticationMethod::getValue).toList();
    final List<String> authorizationGrantTypes = registeredClient.getAuthorizationGrantTypes()
        .stream().map(AuthorizationGrantType::getValue).toList();

    client.setId(registeredClient.getId());
    client.setAuthorizationGrantTypes(
        StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
    client.setClientAuthenticationMethods(
        StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
    client.setClientId(registeredClient.getClientId());
    client.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
    client.setClientName(registeredClient.getClientName());
    client.setClientSecret(registeredClient.getClientSecret());
    client.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
    client.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
    client.setPostLogoutRedirectUris(
        StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
    client.setRedirectUris(
        StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
    client.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
    client.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

    return client;
  }

  private RegisteredClient toObject(Client client) {
    final RegisteredClient.Builder builder = RegisteredClient.withId(client.getId());
    final Set<String> authorizationGrantTypes =
        StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());
    final Set<String> clientAuthenticationMethods =
        StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());
    final Set<String> postLogoutRedirectUris =
        StringUtils.commaDelimitedListToSet(client.getPostLogoutRedirectUris());
    final Set<String> redirectUris = StringUtils.commaDelimitedListToSet(client.getRedirectUris());
    final Set<String> scopes = StringUtils.commaDelimitedListToSet(client.getScopes());
    builder
        .authorizationGrantTypes(clientAuthorizationGrantTypes -> authorizationGrantTypes.stream()
            .forEach(authorizationGrantType -> clientAuthorizationGrantTypes
                .add(resolveAuthorizationGrantType(authorizationGrantType))))
        .clientAuthenticationMethods(authenticationMethods -> clientAuthenticationMethods.stream()
            .forEach(authenticationMethod -> authenticationMethods
                .add(resolveClientAuthenticationMethod(authenticationMethod))))
        .clientId(client.getClientId()).clientIdIssuedAt(client.getClientIdIssuedAt())
        .clientName(client.getClientName()).clientSecret(client.getClientSecret())
        .clientSecretExpiresAt(client.getClientSecretExpiresAt())
        .clientSettings(ClientSettings.withSettings(parseMap(client.getClientSettings())).build())
        .postLogoutRedirectUris(clientPostLogoutRedirectUris -> clientPostLogoutRedirectUris
            .addAll(postLogoutRedirectUris))
        .redirectUris(clientRedirectUris -> clientRedirectUris.addAll(redirectUris))
        .scopes(clientScopes -> clientScopes.addAll(scopes))
        .tokenSettings(TokenSettings.withSettings(parseMap(client.getTokenSettings())).build());

    return builder.build();
  }

  private Map<String, Object> parseMap(String data) {
    try {
      return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
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
    return new AuthorizationGrantType(authorizationGrantType);
  }

  private static ClientAuthenticationMethod resolveClientAuthenticationMethod(
      String clientAuthenticationMethod) {
    if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
    } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue()
        .equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.CLIENT_SECRET_POST;
    } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
      return ClientAuthenticationMethod.NONE;
    }
    return new ClientAuthenticationMethod(clientAuthenticationMethod);
  }
}
