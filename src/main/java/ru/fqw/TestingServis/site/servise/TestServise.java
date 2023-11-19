package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TestRepo;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class TestServise {

  TestRepo testRepo;
  UserServise userServise;

  public Test saveTest(Test test) {
    if (!testRepo.existsById(test.getId())) {
      User user = userServise.getAuthenticationUser();
      test.setCreator(user);
      test.setDateCreated(new Timestamp(System.currentTimeMillis()));
    }
    return testRepo.save(test);
  }

  public Page<Test> getTestsByAuthenticationUser(Pageable pageable) {
    User user = userServise.getAuthenticationUser();
    return testRepo.findByCreator(pageable, user);
  }

  public Test getTestById(Long testId) {
    return testRepo.findById(testId).orElseThrow(
        () -> new ResourceNotFoundException("Тест не найден")
    );
  }

  public Page<Test> getTestsByAuthenticationUserAndNameContaining(Pageable pageable,
      String keywordName) {
    User user = userServise.getAuthenticationUser();
    return testRepo.findByCreatorAndNameContaining(pageable, user, keywordName);
  }


  public void updateTestActivById(Long testId, boolean newActivValue) {
    testRepo.updateTestActivById(testId, newActivValue);
  }

}
