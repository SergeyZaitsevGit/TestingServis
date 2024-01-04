package ru.fqw.TestingServis.bot.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.repo.ResultsTestRepo;
import ru.fqw.TestingServis.bot.service.ResultTestService;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.user.BaseUser;
import ru.fqw.TestingServis.site.service.UserService;

@Service
@AllArgsConstructor
public class ResultTestServiceImpl implements ResultTestService {

  ResultsTestRepo resultsTestRepo;
  UserService userService;
  MongoTemplate mongoTemplate;

  public void saveResult(ResultTest resultTest) {
    resultsTestRepo.save(resultTest);
  }

  @Override
  public ResultTest getResultTestById(String id) {
    return resultsTestRepo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Результат тестирования не найден")
    );
  }

  @Override
  public Page<ResultTest> getResultTestByAuthenticationUser(Pageable pageable) {
    BaseUser user = userService.getAuthenticationUser();
    return resultsTestRepo.findResultTestByTestBaseUser(pageable, user);
  }

  @Override
  public Page<Map.Entry<String, List<ResultTest>>> getTestingResultsGroupedByTestName(Pageable pb,
      String keyword) {
    GroupOperation groupByTestName = Aggregation.group("title").push("$$ROOT").as("resultTest");
    BaseUser user = userService.getAuthenticationUser();
    Criteria matchUserCriteria = Criteria.where("resultTest.test.baseUser.email")
        .is(user.getEmail());
    Aggregation aggregation;
    if (keyword == null) {
      aggregation = Aggregation.newAggregation(groupByTestName,
          Aggregation.match(matchUserCriteria),
          Aggregation.sort(Sort.by(Sort.Direction.DESC, "resultTest.timeStart")));
    } else {
      aggregation = Aggregation.newAggregation(groupByTestName,
          Aggregation.match(matchUserCriteria),
          Aggregation.sort(Sort.by(Sort.Direction.DESC, "resultTest.timeStart")),
          Aggregation.match(Criteria.where("resultTest.title").regex(".*" + keyword + ".*")));
    }
    AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "resultTest", Map.class);
    Pageable pageable = PageRequest.of(pb.getPageNumber(), pb.getPageSize(), Sort.by("title"));
    List<Map> mappedResults = results.getMappedResults();

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), mappedResults.size());

    List<Map> paginatedResults = mappedResults.subList(start, end);
    Map<String, List<ResultTest>> finalResultMap = new LinkedHashMap<>();
    for (Map entry : paginatedResults) {
      String testName = (String) entry.get("_id");
      List<ResultTest> testResults = (List<ResultTest>) entry.get("resultTest");
      finalResultMap.put(testName, testResults);
    }

    List<Map.Entry<String, List<ResultTest>>> paginatedResult = new ArrayList<>(
        finalResultMap.entrySet());
    return new PageImpl<>(paginatedResult, pageable, mappedResults.size());
  }

  @Override
  public boolean existByTitle(String title) {
    return resultsTestRepo.existsByTitle(title);
  }

  public List<ResultTest> getResultsByAuthenticationUserAndTitle(String title) {
    BaseUser baseUser = userService.getAuthenticationUser();
    return resultsTestRepo.findResultTestsByTestBaseUserAndTitle(baseUser, title);
  }

  public List<ResultTest> getResultsByTest(BaseTest baseTest) {
    return resultsTestRepo.findResultTestsByTest(baseTest);
  }
}
