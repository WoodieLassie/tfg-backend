package es.alten.dto;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.alten.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "UserDTO", description = "Data transfer object: user")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends ElvisBaseDTO<User> {

  private static final long serialVersionUID = 883832912345648321L;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @NotNull
  private Long id;

  @NotNull private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  public boolean allFieldsArePresent() {
    return Stream.of(this.email, this.password).allMatch(Objects::nonNull);
  }
}
