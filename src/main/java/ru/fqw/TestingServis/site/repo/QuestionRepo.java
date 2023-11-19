package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.user.User;

import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends CrudRepository<Question, Long> {
    List<Question> findByCreator(User user);
    List<Question> findByType(Type type);
    List<Question> getQuestionByTestSet(Test test);

}
