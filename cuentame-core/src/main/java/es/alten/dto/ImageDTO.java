package es.alten.dto;

import es.alten.domain.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Schema(name = "ImageDTO", description = "Data transfer object. Image")
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageDTO extends ElvisBaseDTO<Image> {
    @Serial private static final long serialVersionUID = 6743758202702488764L;

    @NotNull private String name;
    @NotNull private String type;
    @NotNull private byte[] imageData;
}
