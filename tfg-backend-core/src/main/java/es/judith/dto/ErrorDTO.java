package es.judith.dto;

import lombok.Data;

public @Data class ErrorDTO {
  private String message;
  private Integer index;
}
