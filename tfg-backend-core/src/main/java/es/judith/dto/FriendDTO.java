package es.judith.dto;

import es.judith.domain.Friend;
import es.judith.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "FriendDTO", description = "Data transfer object: friend")
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendDTO extends ElvisBaseDTO<Friend> {

    @Serial
    private static final long serialVersionUID = 1515582535568028098L;

    @NotNull private Long id;
    @NotNull private User userSender;
    @NotNull private User userReceiver;
}
