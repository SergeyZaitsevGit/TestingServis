package ru.fqw.TestingServis.site.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.question.Question;

import java.util.List;

public interface AnswerRepo extends CrudRepository<Answer, Long> {
     List<Answer> findByQuestion(Question question);
}
