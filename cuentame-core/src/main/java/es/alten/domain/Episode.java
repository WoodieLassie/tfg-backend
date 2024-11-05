package es.alten.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "episodes")
public class Episode extends Audit {
  @Serial private static final long serialVersionUID = 8121078370363251363L;

  @Column(name = "episode_num", nullable = false)
  @NotNull
  private Integer episodeNum;

  @Column(name = "title", nullable = false)
  @NotNull
  private String title;

  @Column(name = "summary", nullable = false)
  @NotNull
  private String summary;

  @ManyToOne
  @JoinColumn(name = "season_id", nullable = false)
  private Season season;

  @ManyToMany
  @JoinTable(
      name = "episode_character",
      joinColumns = {@JoinColumn(name = "episode_id")},
      inverseJoinColumns = {@JoinColumn(name = "character_id")})
  private List<Character> characters;
}
