package ru.fqw.TestingServis.bot.repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.user.BaseUser;

public interface ResultsTestRepo extends MongoRepository<ResultTest, String> {

  Page<ResultTest> findResultTestByTestBaseUser(Pageable pageable, BaseUser user);

  boolean existsByTitle(String title);

  List<ResultTest> findResultTestsByTestBaseUserAndTitle(BaseUser baseUser, String title);

  List<ResultTest> findResultTestsByTest(BaseTest baseTest);
}
