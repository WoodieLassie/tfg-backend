package es.alten.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.alten.exceptions.AlreadyExistsException;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotFoundException;
import es.alten.security.CustomPasswordAuthenticationConstants;
import es.alten.security.dao.ClientRepository;
import es.alten.security.domain.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.web.bind.annotation.*;
import es.alten.bo.UserBO;
import es.alten.controller.UserController;
import es.alten.domain.User;
import es.alten.dto.UserDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users")
public class UserControllerImpl extends RestControllerImpl<User, UserDTO, Long, UserBO>
    implements UserController {

  private final RegisteredClientRepository registeredClientRepository;
  private final ClientRepository clientRepository;
  private final PasswordEncoder passwordEncoder;
  private final ObjectMapper objectMapper;

  public UserControllerImpl(RegisteredClientRepository registeredClientRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
    this.registeredClientRepository = registeredClientRepository;
      this.clientRepository = clientRepository;
      this.passwordEncoder = passwordEncoder;
      this.objectMapper = objectMapper;
  }

  @GetMapping("/login")
  public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) throws JsonProcessingException {
    User dbUser = bo.findByEmail(email);
    if (dbUser == null) {
      throw new NotFoundException();
    }
    if (!passwordEncoder.matches(password, dbUser.getPassword())) {
      throw new BadInputException("Password is incorrect");
    }
    Optional<Client> client = clientRepository.findByClientName(dbUser.getEmail());
    if (client.isEmpty()) {
      throw new NotFoundException("Client for this user does not exist");
    }
    ObjectNode response = objectMapper.createObjectNode();
    response.put("clientId", client.get().getClientId());
    String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    return ResponseEntity.ok(jsonResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
    User dbUser = bo.findByEmail(userDTO.getEmail());
    if (dbUser != null) {
      throw new AlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
    }
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    User savedUser = bo.save(userDTO.obtainDomainObject());
    registeredClientRepository.save(
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(savedUser.getId().toString())
            .clientIdIssuedAt(Instant.now())
            .clientSecret(userDTO.getPassword())
            .clientName(userDTO.getEmail())
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(
                CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD)
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope("read")
            .scope("write")
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .tokenSettings(
                TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
            .build());
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
