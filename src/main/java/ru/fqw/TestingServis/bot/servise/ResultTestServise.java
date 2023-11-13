package ru.fqw.TestingServis.bot.servise;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.repo.ResultsTestRepo;
import ru.fqw.TestingServis.site.models.exception.ObjectAlreadyExistsExeption;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.user.BaseUser;
import ru.fqw.TestingServis.site.servise.UserServise;

import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResultTestServise {
    ResultsTestRepo resultsTestRepo;
    UserServise userServise;
    MongoTemplate mongoTemplate;
    public void save(ResultTest resultTest){
        resultsTestRepo.save(resultTest);
    }
    public ResultTest getResultTestById(String id){
        return resultsTestRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Результат тестирования не найден")
        );
    }

    public Page<ResultTest> getResultTestByAuthenticationUser(Pageable pageable){
        BaseUser user = userServise.getAuthenticationUser();
        return resultsTestRepo.findResultTestByTestBaseUser(pageable,user);
    }
    public Map<String, List<ResultTest>> getResultTestByAuthenticationUserSortetByTesting(Pageable pageable){
        Map<String, List<ResultTest>> res = getResultTestByAuthenticationUser(pageable).stream()
                .collect(Collectors.groupingBy(ResultTest::getTitle));
               return res;
    }

    public Page<Map.Entry<String, List<ResultTest>>> getTestingResultsGroupedByTestName(Pageable pb, String keyword) {
        GroupOperation groupByTestName = Aggregation.group("title").push("$$ROOT").as("resultTest");
        BaseUser user = userServise.getAuthenticationUser();
        Criteria matchUserCriteria = Criteria.where("resultTest.test.baseUser.email").is(user.getEmail());
        Aggregation aggregation;
        if (keyword == null){
         aggregation = Aggregation.newAggregation(groupByTestName, Aggregation.match(matchUserCriteria),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "resultTest.timeStart")));
        }
        else {
            aggregation = Aggregation.newAggregation(groupByTestName, Aggregation.match(matchUserCriteria),
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

        List<Map.Entry<String, List<ResultTest>>> paginatedResult = new ArrayList<>(finalResultMap.entrySet());
        return new PageImpl<>(paginatedResult, pageable, mappedResults.size());
    }

    public boolean existByTitle(String title){
        return resultsTestRepo.existsByTitle(title);
    }

}
