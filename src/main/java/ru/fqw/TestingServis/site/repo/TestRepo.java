package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.User;

import java.util.List;
import java.util.Optional;

public interface TestRepo extends CrudRepository<Test, Long> {
    List<Test> findByCreator(User user);
}
