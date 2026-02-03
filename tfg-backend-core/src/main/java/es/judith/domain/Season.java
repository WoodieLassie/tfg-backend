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
@Table(name = "seasons")
public class Season extends ElvisEntity {
  @Serial private static final long serialVersionUID = -789744819989480524L;

  @Column(name = "season_num", nullable = false, unique = true)
  @NotNull
  private Integer seasonNum;

  @Column(name = "description", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String description;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "season", cascade = CascadeType.REMOVE)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<Episode> episodes;

  //En el DTO de lectura NO hace falta mostrar la serie a la que pertenece, pero en el de escritura debe haber un campo para introducir la id de la serie
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "show_id", nullable = false)
  private Show show;
}
