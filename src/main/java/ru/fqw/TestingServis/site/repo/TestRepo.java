package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.user.User;

import java.util.List;

public interface TestRepo extends CrudRepository<Test, Long> {
    List<Test> findByCreator(User user);
}
