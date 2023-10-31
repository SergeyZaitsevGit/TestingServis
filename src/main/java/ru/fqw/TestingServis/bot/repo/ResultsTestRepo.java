package ru.fqw.TestingServis.bot.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.site.models.user.BaseUser;

import java.util.List;

public interface ResultsTestRepo extends MongoRepository<ResultTest, String> {
    List<ResultTest> findResultTestByTestBaseUser(BaseUser user);
}
