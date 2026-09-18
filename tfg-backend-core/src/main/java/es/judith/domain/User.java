package es.judith.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.Set;

/** Entity stores information related to User. */
@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "users")
public class User extends GenericEntity {

  @Serial
  private static final long serialVersionUID = 7389705441694025319L;

  @Column(name = "email", nullable = false, length = 100)
  @NotNull
  @Size(max = 100)
  private String email;

  @Column(name = "username", nullable = false, length = 20)
  @NotNull
  @Size(max = 100)
  private String username;

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

  @OneToMany(mappedBy = "userSender")
  private Set<Friend> friends;

}
