package ru.fqw.TestingServis.site.models.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import ru.fqw.TestingServis.site.models.emuns.Role;

@Data
@MappedSuperclass
public class BaseUser {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
  private Long id;

  private String username;
  private String email;
  private String password;

  private boolean activite;

  private Role role = Role.USER;
}
