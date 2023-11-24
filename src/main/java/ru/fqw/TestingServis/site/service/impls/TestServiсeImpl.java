package ru.fqw.TestingServis.site.service.impls;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TestRepo;

import java.sql.Timestamp;
import ru.fqw.TestingServis.site.service.TestServiсe;
import ru.fqw.TestingServis.site.service.UserServiсe;


@Service
@AllArgsConstructor
public class TestServiсeImpl implements TestServiсe {

  TestRepo testRepo;
  UserServiсe userServiсe;

  @Override
  public Test saveTest(Test test) {
    if (!testRepo.existsById(test.getId())) {
      User user = userServiсe.getAuthenticationUser();
      test.setCreator(user);
      test.setDateCreated(new Timestamp(System.currentTimeMillis()));
    }
    return testRepo.save(test);
  }

  @Override
  public Page<Test> getTestsByAuthenticationUser(Pageable pageable) {
    User user = userServiсe.getAuthenticationUser();
    return testRepo.findByCreator(pageable, user);
  }

  @Override
  public Test getTestById(Long testId) {
    return testRepo.findById(testId).orElseThrow(
        () -> new ResourceNotFoundException("Тест не найден")
    );
  }

  @Override
  public Page<Test> getTestsByAuthenticationUserAndNameContaining(Pageable pageable,
      String keywordName) {
    User user = userServiсe.getAuthenticationUser();
    return testRepo.findByCreatorAndNameContaining(pageable, user, keywordName);
  }

  @Override
  public void updateTestActivById(Long testId, boolean newActivValue) {
    testRepo.updateTestActivById(testId, newActivValue);
  }

}
