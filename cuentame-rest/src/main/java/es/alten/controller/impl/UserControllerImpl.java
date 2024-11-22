package es.alten.controller.impl;

import es.alten.exceptions.AlreadyExistsException;
import es.alten.exceptions.BadInputException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class UserControllerImpl implements UserController {

  private final UserBO bo;
  private final transient PasswordEncoder passwordEncoder;

  public UserControllerImpl(UserBO bo, PasswordEncoder passwordEncoder) {
    this.bo = bo;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping
  @Operation(method = "POST", summary = "Save a new user")
  @ApiResponse(
      responseCode = "201",
      description = "Created",
      content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
      responseCode = "409",
      description = "Conflict",
      content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
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
