package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.User;

import java.util.List;
import java.util.Optional;

public interface TypeRepo extends CrudRepository<Type, Long> {
    Optional<Type> findByName(String name);
    List<Type> findByCreator(User user);

}
