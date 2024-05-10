package ru.fqw.TestingServis.site.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fqw.TestingServis.site.models.test.Test;

public interface TestService {

  Test saveTest(Test test);

  Page<Test> getTestsByAuthenticationUser(Pageable pageable);

  Test getTestById(Long testId);

  Page<Test> getTestsByAuthenticationUserAndNameContaining(Pageable pageable,
      String keywordName);

  void updateTestActivById(Long testId, boolean newActivValue);

  boolean existTestById(Long testId);

  int getMaxBall(Test test);

  int getCountQuestion(Test test);

}
