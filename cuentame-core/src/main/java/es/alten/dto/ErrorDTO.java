package es.alten.dto;

import lombok.Data;

public @Data class ErrorDTO {
  private String message;
  private Integer index;
}
