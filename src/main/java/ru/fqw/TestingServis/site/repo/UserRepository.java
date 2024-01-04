package ru.fqw.TestingServis.site.repo;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.user.User;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByEmail(String email);
}
