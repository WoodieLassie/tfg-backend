package es.judith.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import es.judith.domain.Image;
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

  @NotNull private Long id;
  @NotNull private String name;
  @NotNull private String type;
  @JsonIgnore @NotNull private byte[] imageData;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String imageUrl;
}
