package es.judith.controller.impl;

import es.judith.bo.AuthBO;
import es.judith.bo.JwtBO;
import es.judith.domain.Role;
import es.judith.dto.user.UserDTO;
import es.judith.dto.user.UserInputDTO;
import es.judith.dto.user.UserProfileDTO;
import es.judith.exceptions.AlreadyExistsException;
import es.judith.exceptions.BadInputException;
import es.judith.exceptions.NotExistingIdException;
import es.judith.exceptions.NotFoundException;
import es.judith.utils.ImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.judith.bo.UserBO;
import es.judith.controller.UserController;
import es.judith.domain.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users")

public class UserControllerImpl implements UserController {

  private final UserBO userBO;
  private final transient AuthBO authBO;
  private final transient JwtBO jwtBO;
  private static final Logger LOG = LoggerFactory.getLogger(UserControllerImpl.class);

  public UserControllerImpl(UserBO bo, AuthBO authBO, JwtBO jwtBO) {
    this.userBO = bo;
    this.authBO = authBO;
    this.jwtBO = jwtBO;
  }

  @Override
  @Operation(method = "POST", summary = "Get user token")
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = {@Content(schema = @Schema(hidden = true))})
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody UserInputDTO userInputDTO) {
    boolean areCredentialsCorrect = authBO.verifyCredentials(userInputDTO.getEmail(), userInputDTO.getPassword());
    if (areCredentialsCorrect){
      String token = jwtBO.generateToken(userInputDTO.getEmail());
      Map<String, String> generatedToken = new HashMap<>();
      generatedToken.put("token", token);
      return ResponseEntity.status(HttpStatus.OK).body(generatedToken);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @Override
  @PostMapping("/register")
  @Operation(method = "POST", summary = "Register a new user")
  @ApiResponse(
          responseCode = "201",
          description = "Created",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = {@Content(schema = @Schema(hidden = true))})
  public ResponseEntity<User> register(@RequestBody UserInputDTO userInputDTO) {
    if (!userInputDTO.allFieldsArePresent()) {
      throw new BadInputException("All fields must be present in request body");
    }
    User dbUser = userBO.findByEmail(userInputDTO.getEmail());
    if (dbUser != null) {
      throw new AlreadyExistsException(
              "User with email " + userInputDTO.getEmail() + " already exists");
    }
    dbUser = userBO.findByUsername(userInputDTO.getUsername());
    if (dbUser != null) {
      throw new AlreadyExistsException(
              "User with username " + userInputDTO.getUsername() + " already exists");
    }
    userInputDTO.setPassword(authBO.encryptPassword(userInputDTO.getPassword()));
    userInputDTO.setRole(Role.USER);
    userBO.save(userInputDTO.obtainDomainObject());
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @Operation(method = "GET", summary = "Fetch data of currently logged in user")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = {@Content(schema = @Schema(implementation = UserDTO.class))})
  @ApiResponse(
      responseCode = "401",
      description = "Unauthorized",
      content = {@Content(schema = @Schema(hidden = true))})
  @SecurityRequirement(name = "Authorization")
  @GetMapping
  public ResponseEntity<UserDTO> getLoggedUser() {
    UserDTO userDTO = new UserDTO();
    User user = authBO.getCurrentUser();
    userDTO.loadFromDomain(user);
    return ResponseEntity.status(HttpStatus.OK).body(userDTO);
  }

  @Operation(method = "GET", summary = "Fetch data of another user")
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {@Content(schema = @Schema(implementation = UserProfileDTO.class))})
  @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = {@Content(schema = @Schema(hidden = true))})
  @GetMapping("/{userId}")
  public ResponseEntity<UserProfileDTO> getUser(@PathVariable Long userId) {
    UserProfileDTO userDTO = new UserProfileDTO();
    User user = userBO.findOne(userId);
    if (user == null) {
      throw new NotFoundException();
    }
    userDTO.loadFromDomain(user);
    return ResponseEntity.status(HttpStatus.OK).body(userDTO);
  }

  @Override
  @Operation(
          method = "GET",
          summary = "Get a user image by user identification",
          parameters = @Parameter(ref = "userId"))
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {@Content(mediaType = "image/png", schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @GetMapping(value = "/image/{userId}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> findImageById(@PathVariable Long userId) {
    LOG.debug("UserControllerImpl: Fetching image results with user id {}", userId);
    byte[] image = userBO.findImageById(userId);
    if (image == null || image.length == 0) {
      throw new NotFoundException();
    }
    return ResponseEntity.ok(image);
  }

  @Override
  @Operation(
          method = "PATCH",
          summary = "Edit currently logged in user")
  @ApiResponse(
          responseCode = "204",
          description = "No content",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "409",
          description = "Conflict",
          content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @PatchMapping("/details")
  public ResponseEntity<User> update(@RequestBody UserInputDTO userDTO) {
    User currentUser = authBO.getCurrentUser();
    userDTO.setId(currentUser.getId());
    if (userDTO.getEmail() != null) {
      if (userBO.findByEmail(userDTO.getEmail()) != null) {
        throw new AlreadyExistsException(
                "User with email " + userDTO.getEmail() + " already exists"
        );
      }
      currentUser.setEmail(userDTO.getEmail());
    }
    if (userDTO.getPassword() != null) {
      currentUser.setPassword(authBO.encryptPassword(userDTO.getPassword()));
    }
    if (userDTO.getUsername() != null) {
      if (userBO.findByUsername(userDTO.getUsername()) != null) {
        throw new AlreadyExistsException(
                "User with username " + userDTO.getUsername() + " already exists"
        );
      }
      currentUser.setUsername(userDTO.getUsername());
    }
    userBO.save(currentUser);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @Override
  @Operation(
          method = "PATCH",
          summary = "Edit currently logged in user image")
  @ApiResponse(
          responseCode = "204",
          description = "No content",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "400",
          description = "Bad Request",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> updateImageById(
          @RequestParam("image") MultipartFile file) {
    User user = authBO.getCurrentUser();
    if (file.getSize() == 0) {
      throw new BadInputException("A file must be attached to request");
    }
    if (!Objects.equals(file.getContentType(), "image/png")
            && !Objects.equals(file.getContentType(), "image/jpeg")) {
      throw new BadInputException("File must be png or jpg");
    }
    try {
      user.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    LOG.debug("UserControllerImpl: Modifying image data with user id {}", user.getId());
    userBO.save(user);
    return ResponseEntity.noContent().build();
  }

  @Operation(
          method = "PATCH",
          summary = "Promote user to admin role")
  @ApiResponse(
          responseCode = "204",
          description = "OK",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = {@Content(schema = @Schema(hidden = true))})
  @PatchMapping("/promote/{userId}")
  @SecurityRequirement(name = "Authorization")
  public ResponseEntity<User> promoteUser(@PathVariable Long userId) {
    LOG.debug("UserControllerImpl: Promoting user with id {}", userId);
    userBO.promoteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{userId}")
  @Operation(
          method = "DELETE",
          summary = "Delete user")
  @ApiResponse(
          responseCode = "204",
          description = "No content",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "403",
          description = "Forbidden",
          content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(
          responseCode = "404",
          description = "Not Found",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  public ResponseEntity<User> delete(@PathVariable Long userId) {
    User currentUser = authBO.getCurrentUser();
    if (!Objects.equals(currentUser.getId(), userId) && currentUser.getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    if (userBO.findOne(userId) == null) {
      throw new NotExistingIdException(
              "User with id " + userId + " does not exist"
      );
    }
    LOG.debug("UserControllerImpl: Deleting user with id {}", userId);
    userBO.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
