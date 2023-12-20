package ru.fqw.TestingServis.site.service.impls;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
  EntityManager entityManager;

  @Override
  public List<Question> getQuestionsByAuthenticationUser() {
    return questionRepo.findByCreator(userService.getAuthenticationUser());
  }

  @Override
  public Question saveQuestion(Question question) {
    if (question.getId() == null) {
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

  @Override
  @Transactional
  public Question updateQuestion(Question question){
    Question updatedQuestion = getQuestionById(question.getId());
      if (question.getText() != null) updatedQuestion.setText(question.getText());
      if (question.getType() != null) updatedQuestion.setType(question.getType());
      if (question.getBall() != 0) updatedQuestion.setBall(question.getBall());
   return questionRepo.save(updatedQuestion);
  }

}
