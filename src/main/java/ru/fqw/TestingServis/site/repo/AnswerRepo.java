package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.Answer;
import ru.fqw.TestingServis.site.models.Question;

import java.util.List;

public interface AnswerRepo extends CrudRepository<Answer, Long> {
     List<Answer> findByQuestion(Question question);


}
