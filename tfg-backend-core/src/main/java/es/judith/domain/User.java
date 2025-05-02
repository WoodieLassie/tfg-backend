package es.judith.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;

/** Entity stores information related to User. */
@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "users")
public class User extends ElvisEntity {

  @Serial
  private static final long serialVersionUID = 7389705441694025319L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "email", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String email;

  @Column(name = "password", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String password;

  @Column(name = "role", nullable = false)
  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;

  @Lob
  @Column(name = "image_data", length = 65535)
  @Size(max = 65535)
  private byte[] imageData;
}
