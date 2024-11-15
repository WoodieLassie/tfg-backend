package es.alten.domain;

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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "season_num", nullable = false, unique = true)
  @NotNull
  private Integer seasonNum;

  @Column(name = "description", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String description;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "season")
  @JsonProperty()
  private List<Episode> episodes;
}
