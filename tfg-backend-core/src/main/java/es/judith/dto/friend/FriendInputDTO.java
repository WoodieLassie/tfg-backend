package es.judith.dto.friend;

import es.judith.domain.Friend;
import es.judith.dto.GenericDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Objects;
import java.util.stream.Stream;

@Schema(name = "FriendInputDTO", description = "Data transfer object for input: friend")
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendInputDTO extends GenericDTO<Friend> {
    @JsonIgnore private Long id;
    @JsonIgnore private Long userSenderId;
    @NotNull private Long userReceiverId;
    @JsonIgnore private boolean requestStatus;

    public boolean allFieldsArePresent() {
        return Stream.of(this.userReceiverId).allMatch(Objects::nonNull);
    }
}
