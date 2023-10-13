package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
     Optional<User> findByEmail(String email);
}
