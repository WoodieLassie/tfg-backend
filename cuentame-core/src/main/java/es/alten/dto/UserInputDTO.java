package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.alten.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
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

  public boolean allFieldsArePresent() {
    return Stream.of(this.email, this.password).allMatch(Objects::nonNull);
  }
}
