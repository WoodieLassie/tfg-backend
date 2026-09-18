package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.judith.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "UserDTO", description = "Data transfer object: user profiles")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserProfileDTO extends GenericDTO<User> {

    @Serial
    private static final long serialVersionUID = -7139428087178792784L;

    @NotNull
    private Long id;

    @NotNull private String username;

    @JsonIgnore
    private byte[] imageData;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String imageUrl;
}
