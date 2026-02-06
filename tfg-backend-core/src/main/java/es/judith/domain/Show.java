package es.judith.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "shows")
public class Show extends ElvisEntity {
  @Serial
  private static final long serialVersionUID = 7575277469267296146L;

  @Column(name = "name", nullable = false)
  @NotNull
  private String name;

  @Column(name = "description", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String description;

  @Lob
  @Column(name = "image_data", length = 65535)
  @Size(max = 65535)
  private byte[] imageData;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "show", cascade = CascadeType.REMOVE)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<Season> seasons;

}
