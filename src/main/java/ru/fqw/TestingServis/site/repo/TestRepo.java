package ru.fqw.TestingServis.site.repo;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.user.User;

public interface TestRepo extends CrudRepository<Test, Long> {

  @Override
  @EntityGraph(attributePaths = {"questionSet", "questionSet.answerList"})
  Optional<Test> findById(Long id);

  Page<Test> findByCreator(Pageable pageable, User user);

  Page<Test> findByCreatorAndNameContaining(Pageable pageable, User user, String name);

  @Transactional
  @Modifying
  @Query("UPDATE Test t SET t.activ = :newActivValue WHERE t.id = :testId")
  void updateTestActivById(Long testId, boolean newActivValue);

}
