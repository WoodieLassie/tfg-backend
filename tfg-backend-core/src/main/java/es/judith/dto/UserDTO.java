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

@Schema(name = "UserDTO", description = "Data transfer object: user")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends ElvisBaseDTO<User> {

  private static final long serialVersionUID = 883832912345648321L;

  @NotNull private Long id;

  @NotNull private String email;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Role role;

  @JsonIgnore private String password;
}
