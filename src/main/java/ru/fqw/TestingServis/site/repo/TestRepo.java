package ru.fqw.TestingServis.site.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.user.User;

import java.util.List;

public interface TestRepo extends CrudRepository<Test, Long> {
    Page<Test> findByCreator(Pageable pageable, User user);
    Page<Test> findByCreatorAndNameContaining(Pageable pageable, User user, String name);
}
