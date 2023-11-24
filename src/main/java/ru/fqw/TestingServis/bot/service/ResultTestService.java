package ru.fqw.TestingServis.bot.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.fqw.TestingServis.bot.models.ResultTest;

public interface ResultTestService {
   void saveResult(ResultTest resultTest);
  ResultTest getResultTestById(String id);
  Page<ResultTest> getResultTestByAuthenticationUser(Pageable pageable);
  Page<Map.Entry<String, List<ResultTest>>> getTestingResultsGroupedByTestName(Pageable pb,
      String keyword);
  boolean existByTitle(String title);


}
