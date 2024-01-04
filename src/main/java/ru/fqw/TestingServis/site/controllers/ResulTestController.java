package ru.fqw.TestingServis.site.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fqw.TestingServis.bot.models.AnalysisQuestion;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.service.ResultAnalysisServise;
import ru.fqw.TestingServis.bot.service.ResultTestService;
import ru.fqw.TestingServis.site.service.UserService;


@Controller
@RequiredArgsConstructor
public class ResulTestController {

  final UserService userService;
  final ResultTestService resultTestService;
  final ResultAnalysisServise resultAnalysisServise;

  @GetMapping("/result")
  public String resultTest(Model model, @PageableDefault(size = 20) Pageable pageable,
      @RequestParam("keyword") Optional<String> keyword) {
    Map<String, List<AnalysisQuestion>> analysisQuestionMap = new HashMap<>();
    if (keyword.isPresent()) {
      Page<Map.Entry<String, List<ResultTest>>> resultsGroupedByTestName = resultTestService.getTestingResultsGroupedByTestName(
          pageable, keyword.get());

      model.addAttribute("resultsGroupedByTestName", resultsGroupedByTestName);
      model.addAttribute("keyword", keyword.get());

    } else {
      Page<Map.Entry<String, List<ResultTest>>> resultsGroupedByTestName = resultTestService.getTestingResultsGroupedByTestName(
          pageable, null);
      analysisQuestionMap = resultAnalysisServise.getAnalysisQuestionMapByPage(
          resultsGroupedByTestName);
      model.addAttribute("resultsGroupedByTestName", resultsGroupedByTestName);
    }
    model.addAttribute("analysisQuestionMap", analysisQuestionMap);
    return "result";
  }

  @GetMapping("/result/{resultId}")
  public String resultTestById(Model model, @PathVariable String resultId) {
    ResultTest resultTest = resultTestService.getResultTestById(resultId);
    model.addAttribute("resultTest", resultTest);
    return "resultCurrent";
  }

  @PostMapping("/result/{resultId}")
  public String resultTestById(Model model, @PathVariable String resultId,
      @RequestParam("ballFree") int ballFree,
      @RequestParam("index") int index
  ) {
    ResultTest resultTest = resultTestService.getResultTestById(resultId);
    int oldBall = resultTest.getAnswerFromTelegramUserList().get(index).getBallBehindQuestion();
    resultTest.getAnswerFromTelegramUserList().get(index).setBallBehindQuestion(ballFree);
    resultTest.setBall(resultTest.getBall() + (ballFree - oldBall));
    resultTestService.saveResult(resultTest);
    return "redirect:/result/{resultId}";
  }

}
