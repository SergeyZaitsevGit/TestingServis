package ru.fqw.TestingServis.site.service;

import java.util.List;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;

public interface QuestionService {

  List<Question> getQuestionsByAuthenticationUser();

  List<Question> getQuestionsByType(Type type);

  List<Question> getQuestionsByTest(Test test);

  Question getQuestionById(long questionId);

  void deliteQuestionById(long questionId);

  Question saveQuestion(Question question);

  Question updateQuestion(Question question);

}
