package ru.fqw.TestingServis.site.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.question.Question;

public interface AnswerRepo extends CrudRepository<Answer, Long> {

  List<Answer> findByQuestion(Question question);
}
