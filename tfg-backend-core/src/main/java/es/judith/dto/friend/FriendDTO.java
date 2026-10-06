package es.judith.dto.friend;

import es.judith.domain.Friend;
import es.judith.dto.GenericDTO;
import es.judith.dto.user.UserProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "FriendDTO", description = "Data transfer object: friend")
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendDTO extends GenericDTO<Friend> {

    @Serial
    private static final long serialVersionUID = 1515582535568028098L;

    @NotNull private Long id;
    @NotNull private UserProfileDTO userSender;
    @NotNull private UserProfileDTO userReceiver;
    @NotNull private boolean requestStatus;
}
