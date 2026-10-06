package es.judith.dto.show;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.judith.domain.Show;
import es.judith.dto.GenericDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ShowNoSeasonsDTO", description = "Data transfer object. Show without season info")
@EqualsAndHashCode(callSuper = true)
@Data
public class ShowNoSeasonsDTO extends GenericDTO<Show> {
    @Serial
    private static final long serialVersionUID = 7811169408806519750L;

    @NotNull
    private String name;
    @NotNull private String description;

    @JsonIgnore
    private byte[] imageData;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String imageUrl;
}
