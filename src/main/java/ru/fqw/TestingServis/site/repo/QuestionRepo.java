package ru.fqw.TestingServis.site.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.Question;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.User;

import java.util.List;

public interface QuestionRepo extends CrudRepository<Question, Long> {
    List<Question> findByCreator(User user);
}
