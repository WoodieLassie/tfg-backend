package es.alten.domain;

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
public class Season extends Audit {
  @Serial private static final long serialVersionUID = -789744819989480524L;

  @Column(name = "season_num", nullable = false, unique = true)
  @NotNull
  private Integer seasonNum;

  @Column(name = "description", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String description;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "season")
  private List<Episode> episodes;
}
