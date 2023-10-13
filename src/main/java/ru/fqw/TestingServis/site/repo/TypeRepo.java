package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.Type;

import java.util.Optional;

public interface TypeRepo extends CrudRepository<Type, Long> {
    Optional<Type> findByName(String name);

}
