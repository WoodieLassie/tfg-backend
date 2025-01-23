package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.judith.domain.Role;
import es.judith.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "UserInputDTO", description = "Data transfer object for input: user")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInputDTO extends ElvisBaseDTO<User> {
  @JsonIgnore private Long id;

  @NotNull private String email;

  @NotNull private String password;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Role role;

  public boolean allFieldsArePresent() {
    return Stream.of(this.email, this.password).allMatch(Objects::nonNull);
  }
}
