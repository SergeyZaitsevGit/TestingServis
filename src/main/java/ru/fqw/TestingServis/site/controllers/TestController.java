package ru.fqw.TestingServis.site.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.bot.models.AnalysisQuestion;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.service.ResultAnalysisServise;
import ru.fqw.TestingServis.bot.service.TelegramTestingService;
import ru.fqw.TestingServis.bot.service.TelegramUserService;
import ru.fqw.TestingServis.site.models.*;
import ru.fqw.TestingServis.site.models.exception.ExceptionBody;
import ru.fqw.TestingServis.site.models.exception.ObjectAlreadyExistsExeption;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.repo.TestRepo;
import ru.fqw.TestingServis.site.service.AnswerService;
import ru.fqw.TestingServis.site.service.GroupService;
import ru.fqw.TestingServis.site.service.QuestionService;
import ru.fqw.TestingServis.site.service.TestService;
import ru.fqw.TestingServis.site.service.TypeService;

import java.util.List;
import java.util.Optional;

@RequestMapping
@Controller
@RequiredArgsConstructor
public class TestController {

  final TestService testService;
  final QuestionService questionService;
  final AnswerService answerService;
  final TypeService typeServis;
  final TelegramUserService telegramUserService;
  final TelegramTestingService testingServise;
  final TestRepo testRepo;
  final ResultAnalysisServise resultAnalysisServise;
  final GroupService groupService;

  @GetMapping("/test")
  public String tests(Model model, @PageableDefault(
      sort = {"dateCreated"},
      direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam("keyword") Optional<String> keyword) {
    if (keyword.isPresent()) {
      model.addAttribute("keyword", keyword.get());
      Page<Test> testPage = testService.getTestsByAuthenticationUserAndNameContaining(pageable,
          keyword.get());
      model.addAttribute("testPage", testPage);
    } else {
      Page<Test> testPage = testService.getTestsByAuthenticationUser(pageable);
      model.addAttribute("testPage", testPage);
    }
    return "test";
  }

  @PreAuthorize("@customSecurityExpression.canAccessTest(#testId)")
  @GetMapping("/test/{testId}")
  public String testCurred(@PathVariable Long testId, Model model) {
    Test test = testService.getTestById(testId);
    List<Question> questions = questionService.getQuestionsByTest(test);
    model.addAttribute("test", test);
    model.addAttribute("questions", questions);
    List<Group> groupList = groupService.getByAuthenticationUser();
    model.addAttribute("groupList", groupList);
    model.addAttribute("error", null);
    return "testCurred";
  }

  @PreAuthorize("@customSecurityExpression.canAccessTest(#testId)")
  @GetMapping("/test/analysis/{testId}")
  public String testCurredAnalysis(@PathVariable Long testId, Model model) {
    BaseTest baseTest = testService.getTestById(testId);
    List<AnalysisQuestion> analysisQuestionList = resultAnalysisServise.getQusetionsAnalysesByTest(baseTest);
    model.addAttribute("analysisQuestionList", analysisQuestionList);
    return "testAnalysis";
  }

  @GetMapping("/test/new")
  public String newTest(Model model) {
    model.addAttribute("test", new Test());
    model.addAttribute("question", new Question());
    List<Question> questions = questionService.getQuestionsByAuthenticationUser();
    List<Type> types = typeServis.getTypeByAuthenticationUser();
    model.addAttribute("types", types);
    model.addAttribute("questions", questions);
    return "testNew";
  }

  @GetMapping("/test/edit/{testId}")
  @PreAuthorize("@customSecurityExpression.canAccessTest(#testId)")
  public String testEdit(@PathVariable Long testId, Model model) {
    Test test = testService.getTestById(testId);
    List<Question> questions = questionService.getQuestionsByAuthenticationUser();
    List<Type> types = typeServis.getTypeByAuthenticationUser();
    model.addAttribute("test", test);
    model.addAttribute("questions", questions);
    model.addAttribute("types", types);
    return "testEdit";
  }

  @PreAuthorize("@customSecurityExpression.canAccessTelegramUser(#tgUsersResult)")
  @PostMapping("/test/{testId}")
  public String testCurred(@PathVariable Long testId,
      @RequestParam("checked") List<Long> tgUsersResult,
      @RequestParam("title") String title,
      @RequestParam(value = "mixQ", required = false) boolean mixQ,
      @RequestParam(value = "mixA", required = false) boolean mixA,
      Model model) {
    try {
      Test test = testService.getTestById(testId);
      test.setMixQuestions(mixQ);
      test.setMixAnswers(mixA);
      test.setActiv(true);
      testService.saveTest(test);
      testingServise.startTest(tgUsersResult, test, title);
    } catch (ObjectAlreadyExistsExeption objectAlreadyExistsExeption) {
      ExceptionBody error = new ExceptionBody(objectAlreadyExistsExeption.getMessage());
      Test test = testService.getTestById(testId);
      List<Question> questions = questionService.getQuestionsByTest(test);
      model.addAttribute("test", test);
      model.addAttribute("questions", questions);
      List<TelegramUser> telegramUsers = telegramUserService.getTelegramUserByAuthenticationUser();
      model.addAttribute("telegramUsers", telegramUsers);
      model.addAttribute("error", error);
      return "testCurred";
    }
    return "redirect:/test/" + testId;
  }

  @PostMapping("/test/new")
  public String newTest(
      @ModelAttribute("test")
      @Valid Test test, Errors errors,
      Model model
  ) {
    if (errors.hasErrors()) {
      model.addAttribute("question", new Question());
      Iterable<Question> questions = questionService.getQuestionsByAuthenticationUser();
      model.addAttribute("questions", questions);
      return "testNew";
    }
    testService.saveTest(test);
    return "redirect:/test";
  }

}
