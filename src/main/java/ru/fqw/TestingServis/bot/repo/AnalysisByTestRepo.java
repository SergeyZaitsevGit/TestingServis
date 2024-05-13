package ru.fqw.TestingServis.bot.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.fqw.TestingServis.bot.models.AnalysisByTesting;
import ru.fqw.TestingServis.site.models.user.BaseUser;

public interface AnalysisByTestRepo extends MongoRepository<AnalysisByTesting, String> {
    AnalysisByTesting findByTestingAndBaseUser(String title, BaseUser baseUser);
}
