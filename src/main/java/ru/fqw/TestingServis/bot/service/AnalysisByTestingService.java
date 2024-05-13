package ru.fqw.TestingServis.bot.service;

import ru.fqw.TestingServis.bot.models.AnalysisByTesting;
import ru.fqw.TestingServis.site.models.user.BaseUser;

public interface AnalysisByTestingService {
    AnalysisByTesting save(AnalysisByTesting analysisByTesting);

    AnalysisByTesting getByTitleAndBaseUser(String title, BaseUser baseUser);
}
