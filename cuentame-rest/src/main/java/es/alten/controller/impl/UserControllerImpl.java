package es.alten.controller.impl;

import es.alten.exceptions.AlreadyExistsException;
import es.alten.exceptions.BadInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import es.alten.bo.UserBO;
import es.alten.controller.UserController;
import es.alten.domain.User;
import es.alten.dto.UserDTO;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users")
public class UserControllerImpl
    implements UserController {

  private final UserBO bo;
  private final PasswordEncoder passwordEncoder;

  public UserControllerImpl(UserBO bo, PasswordEncoder passwordEncoder) {
      this.bo = bo;
      this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
    if (!userDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    User dbUser = bo.findByEmail(userDTO.getEmail());
    if (dbUser != null) {
      throw new AlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
    }
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    bo.save(userDTO.obtainDomainObject());
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
