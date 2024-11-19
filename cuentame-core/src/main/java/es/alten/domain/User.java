package es.alten.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/** Entity stores information related to User. */
@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "users")
public class User extends Audit {

  private static final long serialVersionUID = 7389705441694025319L;

  @Column(name = "email", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String email;

  @Column(name = "password", nullable = false, length = 100)
  private String password;

}
