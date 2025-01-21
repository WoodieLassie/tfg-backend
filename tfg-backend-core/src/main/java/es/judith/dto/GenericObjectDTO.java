package es.judith.dto;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "GenericObjectDTO", description = "Transfer object: Generic")
public @Data class GenericObjectDTO implements Serializable {

  private static final long serialVersionUID = 2463786643424711916L;

  private Long id;

  private String name;
}
