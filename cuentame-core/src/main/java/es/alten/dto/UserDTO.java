package es.alten.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @NotNull private Long id;

  @NotNull private String email;

  @JsonIgnore private String password;
}
