package es.judith.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.sql.Date;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "actors")
public class Actor extends Audit {
  @Serial private static final long serialVersionUID = 1887096612684365214L;

  @Column(name = "name", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String name;

  @Column(name = "birth_date", nullable = false)
  @NotNull
  private Date birthDate;

  @Column(name = "nationality", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String nationality;

  @Column(name = "gender", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String gender;

  @Column(name = "birth_location", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String birthLocation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "character_id", nullable = false)
  private Character character;

  @Lob
  @Column(name = "image_data", length = 65535)
  @Size(max = 65535)
  private byte[] imageData;
}
