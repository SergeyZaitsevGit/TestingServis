package ru.fqw.TestingServis.bot.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.bot.models.AnalysisQuestion;
import ru.fqw.TestingServis.bot.models.AnswerFromTelegramUser;
import ru.fqw.TestingServis.bot.models.AnswerWhithSelect;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.service.ResultAnalysisServise;
import ru.fqw.TestingServis.bot.service.ResultTestService;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.service.AnswerService;
import ru.fqw.TestingServis.site.service.TestService;


@Service
@RequiredArgsConstructor
public class ResultAnalysisServiseImpl implements ResultAnalysisServise {

  private  final TestService testService;
  private  final AnswerService answerService;
  private  final ResultTestService resultTestService;

  /**
   * Метод возвращает список вопросов с данными для аналитики по названию тестирования
   **/
  @Override
  @Transactional
  public List<AnalysisQuestion> getQusetionsAnalysesByTitleTesting(String titleTesting) {
    List<ResultTest> resultTests = resultTestService.getResultsByAuthenticationUserAndTitle(
        titleTesting);
    return analysisQuestionByResult(resultTests);
  }

  @Override
  @Transactional
  public List<AnalysisQuestion> getQusetionsAnalysesByTest(BaseTest test) {
    return analysisQuestionByResult(resultTestService.getResultsByTest(test));
  }

  /**
   * Метод возвращает мапу вопросов с данными для аналитики по странице с результатами<p> Анализ
   * происходит для всех тестирований имеющихся на странице
   **/

  @Override
  @Transactional
  public Map<String, List<AnalysisQuestion>> getAnalysisQuestionMapByPage(
      Page<Entry<String, List<ResultTest>>> resultsGroupedByTestName) {
    Map<String, List<AnalysisQuestion>> analysisQuestionMap = new HashMap<>();
    resultsGroupedByTestName.get().forEach(
        res -> analysisQuestionMap
            .put(
                res.getKey(),
                new ArrayList<>(getQusetionsAnalysesByTitleTesting(res.getKey())
                )
            )
    );
    return analysisQuestionMap;
  }

  /**
   * Метод возвращает список вопросов с данными для аналитики по списку результатов<p> Происходит
   * подсчет среднего балла за вопрос, <p> Процента выполнения вопроса,<p> Сортировка итогового
   * списка про проценту выполнения
   **/
  private List<AnalysisQuestion> analysisQuestionByResult(List<ResultTest> resultTests) {
    if (!testService.existTestById(resultTests.get(0).getTest().getId())) {
      return new ArrayList<>();
    }
    Test test = testService.getTestById(resultTests.get(0).getTest().getId());
    List<AnalysisQuestion> analysisQuestionList = test.getQuestionSet().stream()
        .map(AnalysisQuestion::new).toList(); //Инициализируем итоговый лист вопросов с аналитикой
    Map<Long, List<Integer>> idQuestionListBallMap = new HashMap<>();//Мапа для подсчета среднего балла у каждого вопроса
    Map<Long, Map<Long, Integer>> questionIdByMapAnswerIdCount = new HashMap<>();//Мапа для посчета самых частых ответов у каждого вопроса

    for (ResultTest res : resultTests) {
      for (AnswerFromTelegramUser answer : res.getAnswerFromTelegramUserList()) {
        BaseQuestion question = answer.getQuestion();
        idQuestionListBallMap.computeIfAbsent(question.getId(), l -> new ArrayList<>())
            .add(answer.getBallBehindQuestion());

        answer.getAnswerWhithSelect().stream().filter(AnswerWhithSelect::isSelected).forEach(
            a -> questionIdByMapAnswerIdCount.computeIfAbsent(question.getId(),
                m -> new HashMap<>()).put(a.getId(),
                questionIdByMapAnswerIdCount.getOrDefault(question.getId(), new HashMap<>())
                    .getOrDefault(a.getId(), 0) + 1)
        );


      }
    }

    analysisQuestionList = analysisQuestionList.stream() //Заполняем средний балл
        .peek(q -> q.setAvgBall(
            BigDecimal.valueOf(idQuestionListBallMap.computeIfAbsent(q.getBaseQuestion().getId(),
                    i -> new ArrayList<>(List.of(1)))
                .stream().mapToDouble(i -> i).sum() / idQuestionListBallMap.get(
                q.getBaseQuestion().getId()).size()).setScale(1, RoundingMode.CEILING)
        )).toList();

    analysisQuestionList = analysisQuestionList.stream()
        .peek(q -> q.setPercentCorrected( //Заполняем процент выполнения
            BigDecimal.valueOf((q.getAvgBall().doubleValue() / q.getBaseQuestion().getBall()) * 100)
                .setScale(1, RoundingMode.CEILING)
        )).toList();

    for (AnalysisQuestion analysisQuestion : analysisQuestionList) {//Заполянем самые частые ответы
      if (questionIdByMapAnswerIdCount.get(
          analysisQuestion.getBaseQuestion().getId()) != null) {
        Map<Long, Integer> answerMap = questionIdByMapAnswerIdCount.get(
            analysisQuestion.getBaseQuestion().getId());
        int maxCount = answerMap.entrySet().stream().max(Map.Entry.comparingByValue()).get()
            .getValue();
        List<BaseAnswer> answerList = new ArrayList<>();
        answerMap.entrySet().stream().filter(m -> m.getValue().equals(maxCount))
            .forEach(entry -> analysisQuestion.getBaseQuestion().getBaseAnswerList()
                .stream()
                .filter(a -> a.getId().equals(entry.getKey()))
                .findFirst()
                .ifPresent(answerList::add)
            );
        analysisQuestion.setMostFrequentAnswers(answerList);
      }
    }

    analysisQuestionList = analysisQuestionList.stream()
        .sorted(Comparator.comparingDouble(a -> a.getPercentCorrected().doubleValue())).collect(
            Collectors.toList());
    return analysisQuestionList;
  }

  private List<ResultTest> getBestsResult(List<ResultTest> resultTests) {
    int maxBall = resultTests.stream()
        .max(Comparator.comparingInt(ResultTest::getBall))
        .get().getBall();

    return resultTests.stream().filter(resultTest -> resultTest.getBall() == maxBall).toList();
  }

  private List<ResultTest> getBadResult(List<ResultTest> resultTests) {
    int minBall = resultTests.stream()
        .min(Comparator.comparingInt(ResultTest::getBall))
        .get().getBall();

    return resultTests.stream().filter(resultTest -> resultTest.getBall() == minBall).toList();
  }
}
