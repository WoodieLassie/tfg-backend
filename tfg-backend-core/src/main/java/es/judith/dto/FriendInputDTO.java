package es.judith.dto;

import es.judith.domain.Friend;
import es.judith.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

@Schema(name = "FriendInputDTO", description = "Data transfer object for input: friend")
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendInputDTO extends ElvisBaseDTO<Friend> {
    @JsonIgnore private Long id;
    @JsonIgnore private User userSender;
    @NotNull private User userReceiver;
}
