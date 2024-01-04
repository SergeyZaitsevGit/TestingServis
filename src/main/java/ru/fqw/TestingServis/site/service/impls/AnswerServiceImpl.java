package ru.fqw.TestingServis.site.service.impls;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.repo.AnswerRepo;
import ru.fqw.TestingServis.site.service.AnswerService;

@Service
@AllArgsConstructor
public class AnswerServiceImpl implements AnswerService {

  AnswerRepo answerRepo;

  @Override
  public Answer saveAnswer(Answer answer, Question question) {
    answer.setQuestion(question);
    return answerRepo.save(answer);
  }

  @Override
  public void delAnswerById(long answerId) {
    answerRepo.deleteById(answerId);
  }

  @Override
  public Answer getAnswerById(long answerId) {
    return answerRepo.findById(answerId).orElseThrow(
        () -> new ResourceNotFoundException("Вариант ответа не найден")
    );
  }

  @Override
  public List<Answer> getAnswersByQuestion(Question question) {
    return answerRepo.findByQuestion(question);
  }
}
