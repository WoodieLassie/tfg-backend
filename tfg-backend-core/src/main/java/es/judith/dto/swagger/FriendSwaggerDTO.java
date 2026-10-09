package es.judith.dto.swagger;

import es.judith.domain.Friend;
import es.judith.dto.GenericDTO;
import es.judith.dto.user.UserProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Schema(name = "FriendSwaggerDTO", description = "Data transfer object for swagger: friend")
@EqualsAndHashCode(callSuper = true)
@Data
public class FriendSwaggerDTO extends GenericDTO<Friend> {
    private Map<Long, UserProfileDTO> result;
}
