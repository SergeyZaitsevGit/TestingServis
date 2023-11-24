package ru.fqw.TestingServis.site.service.impls;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.QuestionRepo;
import java.util.ArrayList;
import java.util.List;
import ru.fqw.TestingServis.site.service.QuestionService;
import ru.fqw.TestingServis.site.service.UserService;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

  QuestionRepo questionRepo;
  UserService userService;

  @Override
  public List<Question> getQuestionsByAuthenticationUser() {
    return questionRepo.findByCreator(userService.getAuthenticationUser());
  }

  @Override
  public Question saveQuestion(Question question) {
    if (!questionRepo.existsById(question.getId())) {
      User user = userService.getAuthenticationUser();
      question.setCreator(user);
    }
    return questionRepo.save(question);
  }

  @Override
  public List<Question> getQuestionsByType(Type type) {
    return questionRepo.findByType(type);
  }

  @Override
  public List<Question> getQuestionsByTest(Test test) {
    return new ArrayList<>(test.getQuestionSet());
  }

  @Override
  public Question getQuestionById(long questionId) {
    return questionRepo.findById(questionId)
        .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
  }

  @Override
  public void deliteQuestionById(long questionId) {
    questionRepo.deleteById(questionId);
  }

}
