package ru.fqw.TestingServis.bot.repo;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.user.BaseUser;

import java.util.List;
import java.util.Map;

public interface ResultsTestRepo extends MongoRepository<ResultTest, String> {
    Page<ResultTest> findResultTestByTestBaseUser(Pageable pageable, BaseUser user);
    Page<ResultTest> findResultTestByTestBaseUserAndTest(Pageable pageable, BaseUser user, BaseTest test);
    boolean existsByTitle(String title);
  //  Page<Map<String, List<ResultTest>>> findAllByTestBaseUser(Pageable pageable,BaseUser user);
}
