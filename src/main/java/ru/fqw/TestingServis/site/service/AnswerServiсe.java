package ru.fqw.TestingServis.site.service;

import java.util.List;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.question.Question;

public interface AnswerServiсe {

  Answer saveAnswer(Answer answer, Question question);

  void delAnswerById(long answerId);

  Answer getAnswerById(long answerId);

  List<Answer> getAnswersByQuestion(Question question);


}
