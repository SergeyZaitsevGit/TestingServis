package ru.fqw.TestingServis.site.service.impls;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.bot.healpers.TimeUtils;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TestRepo;
import ru.fqw.TestingServis.site.service.QuestionService;
import ru.fqw.TestingServis.site.service.TestService;
import ru.fqw.TestingServis.site.service.UserService;


@Service
@AllArgsConstructor
public class TestServiceImpl implements TestService {

  private TestRepo testRepo;
  private UserService userService;
  private QuestionService questionService;
  private TimeUtils timeUtils;

  @Override
  @Transactional
  public Test saveTest(Test test) {
    if (test.getId() == null || !testRepo.existsById(test.getId())) {
      User user = userService.getAuthenticationUser();
      test.setCreator(user);
      test.setDateCreated(timeUtils.getNow());
    }
    test.setMaxBall(getMaxBall(test));
    test.setCountQuestion(getCountQuestion(test));
    return testRepo.save(test);
  }

  @Override
  @Transactional
  public Page<Test> getTestsByAuthenticationUser(Pageable pageable) {
    User user = userService.getAuthenticationUser();
    return testRepo.findByCreator(pageable, user);
  }

  @Override
  @Transactional
  public Test getTestById(Long testId) {
    return testRepo.findById(testId).orElseThrow(
        () -> new ResourceNotFoundException("Тест не найден")
    );
  }

  @Override
  @Transactional
  public Page<Test> getTestsByAuthenticationUserAndNameContaining(Pageable pageable,
      String keywordName) {
    User user = userService.getAuthenticationUser();
    return testRepo.findByCreatorAndNameContaining(pageable, user, keywordName);
  }

  @Override
  @Transactional
  public void updateTestActivById(Long testId, boolean newActivValue) {
    testRepo.updateTestActivById(testId, newActivValue);
  }

  @Override
  @Transactional
  public boolean existTestById(Long testId) {
    return testRepo.existsById(testId);
  }

  @Override
  @Transactional
  public int getMaxBall(Test test) {
    return test.getQuestionSet()
            .stream()
            .mapToInt(BaseQuestion::getBall)
            .sum();
  }

  @Override
  public int getCountQuestion(Test test) {
    return test.getQuestionSet().size();
  }
}
