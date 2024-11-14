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
@Table(name = "episodes")
public class Episode extends ElvisEntity {
  @Serial private static final long serialVersionUID = 8121078370363251363L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "episode_num", nullable = false)
  @NotNull
  private Integer episodeNum;

  @Column(name = "title", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String title;

  @Column(name = "summary", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String summary;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "season_id", nullable = false)
  private Season season;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "episode_character",
      joinColumns = {@JoinColumn(name = "episode_id")},
      inverseJoinColumns = {@JoinColumn(name = "character_id")})
  private List<Character> characters;
}
