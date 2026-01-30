package es.judith.controller.impl;

import es.judith.bo.JwtBO;
import es.judith.domain.Role;
import es.judith.dto.UserDTO;
import es.judith.dto.UserInputDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import es.judith.bo.UserBO;
import es.judith.controller.UserController;
import es.judith.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users")
public class UserControllerImpl extends GenericControllerImpl implements UserController {

  private final UserBO userBO;
  private final JwtBO jwtBO;
  private static final Logger LOG = LoggerFactory.getLogger(UserControllerImpl.class);

  public UserControllerImpl(UserBO bo, JwtBO jwtBO) {
    super(bo);
    this.userBO = bo;
    this.jwtBO = jwtBO;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody UserInputDTO userInputDTO) {
    boolean areCredentialsCorrect = userBO.verify(userInputDTO.getEmail(), userInputDTO.getPassword());
    if (areCredentialsCorrect){
      Role userRole = userBO.getRoleByEmail(userInputDTO.getEmail());
      String token = jwtBO.generateToken(userInputDTO.getEmail(), userRole.toString());
      Map<String, String> generatedToken = new HashMap<>();
      generatedToken.put("token", token);
      return ResponseEntity.status(HttpStatus.OK).body(generatedToken);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PostMapping("/register")
  @Operation(method = "POST", summary = "Save a new user")
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
    userInputDTO.setPassword(userBO.encryptPassword(userInputDTO.getPassword()));
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
    UserDTO userDTO = this.getCurrentUser();
    if (userDTO == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    return ResponseEntity.status(HttpStatus.OK).body(userDTO);
  }

  @Operation(method = "GET", summary = "Fetch data of another user")
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {@Content(schema = @Schema(implementation = UserDTO.class))})
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
    UserDTO userDTO = new UserDTO();
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
          parameters = @Parameter(ref = "id"))
  @ApiResponse(
          responseCode = "200",
          description = "OK",
          content = {@Content(mediaType = "image/png", schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> findImageById(@PathVariable Long id) {
    LOG.debug("UserControllerImpl: Fetching image results with user id {}", id);
    byte[] image = userBO.findImageById(id);
    if (image == null || image.length == 0) {
      throw new NotFoundException();
    }
    return ResponseEntity.ok(image);
  }

  @Override
  @Operation(
          method = "PATCH",
          summary = "Edit an existing user image",
          parameters = @Parameter(ref = "id"))
  @ApiResponse(
          responseCode = "204",
          description = "No content",
          content = {@Content(schema = @Schema(hidden = true))})
  @ApiResponse(
          responseCode = "404",
          description = "Not found",
          content = @Content(schema = @Schema(hidden = true)))
  @SecurityRequirement(name = "Authorization")
  @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<User> updateImageById(
          @PathVariable Long id, @RequestParam("image") MultipartFile file) {
    if (file.getSize() == 0) {
      throw new BadInputException("A file must be attached to request");
    }
    if (!Objects.equals(file.getContentType(), "image/png")
            && !Objects.equals(file.getContentType(), "image/jpeg")) {
      throw new BadInputException("File must be png or jpg");
    }
    User user = userBO.findOne(id);
    if (user == null) {
      throw new NotExistingIdException("User with id " + id + " does not exist");
    }
    try {
      user.setImageData(ImageUtil.compressImage(file.getBytes()));
    } catch (IOException e) {
      throw new BadInputException(e);
    }
    LOG.debug("UserControllerImpl: Modifying image data with user id {}", id);
    userBO.save(user);
    return ResponseEntity.noContent().build();
  }
}
