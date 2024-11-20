package es.alten.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.alten.exceptions.AlreadyExistsException;
import es.alten.exceptions.BadInputException;
import es.alten.exceptions.NotFoundException;
import es.alten.security.dao.ClientRepository;
import es.alten.security.domain.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.*;
import es.alten.bo.UserBO;
import es.alten.controller.UserController;
import es.alten.domain.User;
import es.alten.dto.UserDTO;
import es.alten.rest.impl.RestControllerImpl;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users")
public class UserControllerImpl extends RestControllerImpl<User, UserDTO, Long, UserBO>
    implements UserController {

  private final PasswordEncoder passwordEncoder;

  public UserControllerImpl(RegisteredClientRepository registeredClientRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
      this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
    User dbUser = bo.findByEmail(userDTO.getEmail());
    if (dbUser != null) {
      throw new AlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
    }
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    bo.save(userDTO.obtainDomainObject());
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
