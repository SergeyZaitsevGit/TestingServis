package ru.fqw.TestingServis.bot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.bot.models.AnalysisByTesting;
import ru.fqw.TestingServis.bot.models.AnalysisQuestion;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.models.ResultsByTestingWithAnalysis;
import ru.fqw.TestingServis.bot.repo.ResultsTestRepo;
import ru.fqw.TestingServis.bot.service.AnalysisByTestingService;
import ru.fqw.TestingServis.bot.service.ResultTestService;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.user.BaseUser;
import ru.fqw.TestingServis.site.service.UserService;

@Service
@RequiredArgsConstructor
public class ResultTestServiceImpl implements ResultTestService {

  private  final ResultsTestRepo resultsTestRepo;
  private  final UserService userService;
  private  final MongoTemplate mongoTemplate;
  private final AnalysisByTestingService analysisByTestingService;

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
  @Transactional
  public Page<Map.Entry<String, ResultsByTestingWithAnalysis>> getTestingResultsGroupedByTestName(Pageable pb,
                                                                                                  String keyword) {
    BaseUser baseUser = userService.getAuthenticationUser();
    Page<String> titles = getDistinctTitles(pb,keyword, baseUser);
    List<Map.Entry<String, ResultsByTestingWithAnalysis>> result = new ArrayList<>();
    for (String title:titles) {
      List<ResultTest> resultTests = resultsTestRepo.findResultTestsByTestBaseUserAndTitle(baseUser,title);
      AnalysisByTesting analysisByTesting =  analysisByTestingService.getByTitleAndBaseUser(title, baseUser);
      List<AnalysisQuestion> analysisQuestionList = new ArrayList<>();
      if (analysisByTesting != null){
        analysisQuestionList.addAll(analysisByTesting.getAnalysisQuestionList());
      }
      result.add(Map.entry(title, new ResultsByTestingWithAnalysis(resultTests,analysisQuestionList)));
    }
    return new PageImpl<>(result,pb,titles.getTotalElements());
  }

  @Override
  public boolean existByTitle(String title) {
    return resultsTestRepo.existsByTitle(title);
  }

  public List<ResultTest> getResultsByAuthenticationUserAndTitle(String title) {
    BaseUser baseUser = userService.getAuthenticationUser();
    return resultsTestRepo.findResultTestsByTestBaseUserAndTitle(baseUser, title);
  }

  @Override
  public List<ResultTest> getResultsByUserAndTitle(BaseUser baseUser, String title) {
    return resultsTestRepo.findResultTestsByTestBaseUserAndTitle(baseUser, title);
  }

  public List<ResultTest> getResultsByTest(BaseTest baseTest) {
    return resultsTestRepo.findResultTestsByTestId(baseTest.getId());
  }

  @Transactional
  public Page<String> getDistinctTitles(Pageable pageable, String keyword, BaseUser user){
    GroupOperation groupBy
            = Aggregation.group("title").push("$$ROOT").as("resultTest");
    SortOperation sortOp
            = Aggregation.sort(Sort.by(Sort.Direction.DESC, "resultTest.timeStart"));
    SkipOperation skipOp
            = Aggregation.skip(pageable.getPageNumber() * pageable.getPageSize() * 1L);
    LimitOperation limitOp
            = Aggregation.limit(pageable.getPageSize());
    Criteria matchUserCriteria = Criteria.where("resultTest.test.baseUser.email")
            .is(user.getEmail());

    Aggregation aggregation;

    if (keyword == null){
     aggregation = Aggregation.newAggregation(
              groupBy,
              Aggregation.match(matchUserCriteria),
              sortOp,
              skipOp,
              limitOp
      );
    }
    else {
      aggregation = Aggregation.newAggregation(
              groupBy,
              Aggregation.match(matchUserCriteria),
              sortOp,
              skipOp,
              limitOp,
              Aggregation.match(Criteria.where("resultTest.title").regex(".*" + keyword + ".*")));
    }

    AggregationResults<Document> distinctTitles
            = mongoTemplate.aggregate(aggregation, "resultTest", Document.class);
    List<String> res = distinctTitles.getMappedResults().stream()
            .map(document -> document.get("_id").toString()).toList();

    Aggregation aggregationCount = Aggregation.newAggregation(
            groupBy,
            Aggregation.match(matchUserCriteria),
            Aggregation.count().as("total")
    );
    AggregationResults<Document> countResult
            = mongoTemplate.aggregate(aggregationCount, "resultTest", Document.class);
    Integer totalElements = countResult.getMappedResults().get(0).getInteger("total");
    return new PageImpl<>(res,pageable,totalElements);
  }
}
