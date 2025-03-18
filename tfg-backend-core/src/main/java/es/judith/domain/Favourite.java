package es.judith.domain;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "favourites")
public class Favourite extends Audit {
  @Serial private static final long serialVersionUID = -3076389732947119269L;

  @JoinColumn(name = "show_id", nullable = false)
  @OneToOne
  private Show show;
}
