package ru.fqw.TestingServis.bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.bot.models.AnalysisByTesting;
import ru.fqw.TestingServis.bot.repo.AnalysisByTestRepo;
import ru.fqw.TestingServis.bot.service.AnalysisByTestingService;
import ru.fqw.TestingServis.site.models.user.BaseUser;

@Service
@RequiredArgsConstructor
public class AnalysisByTestingServiceImpl implements AnalysisByTestingService {

    private final AnalysisByTestRepo analysisByTestRepo;

    @Override
    @Transactional
    public AnalysisByTesting save(AnalysisByTesting analysisByTesting) {
        return analysisByTestRepo.save(analysisByTesting);
    }

    @Override
    @Transactional
    public AnalysisByTesting getByTitleAndBaseUser(String title, BaseUser baseUser) {
        return analysisByTestRepo.findByTestingAndBaseUser(title, baseUser);
    }
}
