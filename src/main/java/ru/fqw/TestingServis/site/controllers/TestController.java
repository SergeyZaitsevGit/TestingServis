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
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.serviсe.TelegramTestingServiсe;
import ru.fqw.TestingServis.bot.serviсe.TelegramUserServiсe;
import ru.fqw.TestingServis.site.models.*;
import ru.fqw.TestingServis.site.models.exception.ExceptionBody;
import ru.fqw.TestingServis.site.models.exception.ObjectAlreadyExistsExeption;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.repo.TestRepo;
import ru.fqw.TestingServis.site.service.AnswerServiсe;
import ru.fqw.TestingServis.site.service.QuestionServiсe;
import ru.fqw.TestingServis.site.service.TestServiсe;
import ru.fqw.TestingServis.site.service.TypeServiсe;

import java.util.List;
import java.util.Optional;

@RequestMapping
@Controller
@RequiredArgsConstructor
public class TestController {

  final TestServiсe testServiсe;
  final QuestionServiсe questionServiсe;
  final AnswerServiсe answerServiсe;
  final TypeServiсe typeServis;
  final TelegramUserServiсe telegramUserServiсe;
  final TelegramTestingServiсe testingServise;
  final TestRepo testRepo;

  @GetMapping("/test")
  public String tests(Model model, @PageableDefault(
      sort = {"dateCreated"},
      direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam("keyword") Optional<String> keyword) {
    if (keyword.isPresent()) {
      model.addAttribute("keyword", keyword.get());
      Page<Test> testPage = testServiсe.getTestsByAuthenticationUserAndNameContaining(pageable,
          keyword.get());
      model.addAttribute("testPage", testPage);
    } else {
      Page<Test> testPage = testServiсe.getTestsByAuthenticationUser(pageable);
      model.addAttribute("testPage", testPage);
    }
    return "test";
  }

  @PreAuthorize("@customSecurityExpression.canAccessTest(#testId)")
  @GetMapping("/test/{testId}")
  public String testCurred(@PathVariable Long testId, Model model) {
    Test test = testServiсe.getTestById(testId);
    List<Question> questions = questionServiсe.getQuestionsByTest(test);
    model.addAttribute("test", test);
    model.addAttribute("questions", questions);
    List<TelegramUser> telegramUsers = telegramUserServiсe.getTelegramUserByAuthenticationUser();
    model.addAttribute("telegramUsers", telegramUsers);
    model.addAttribute("error", null);
    return "testCurred";
  }

  @GetMapping("/test/new")
  public String newTest(Model model) {
    model.addAttribute("test", new Test());
    model.addAttribute("question", new Question());
    List<Question> questions = questionServiсe.getQuestionsByAuthenticationUser();
    List<Type> types = typeServis.getTypeByAuthenticationUser();
    model.addAttribute("types", types);
    model.addAttribute("questions", questions);
    return "testNew";
  }

  @GetMapping("/test/edit/{testId}")
  @PreAuthorize("@customSecurityExpression.canAccessTest(#testId)")
  public String testEdit(@PathVariable Long testId, Model model) {
    Test test = testServiсe.getTestById(testId);
    List<Question> questions = questionServiсe.getQuestionsByAuthenticationUser();
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
      Test test = testServiсe.getTestById(testId);
      test.setMixQuestions(mixQ);
      test.setMixAnswers(mixA);
      test.setActiv(true);
      testServiсe.saveTest(test);
      testingServise.startTest(tgUsersResult, test, title);
    } catch (ObjectAlreadyExistsExeption objectAlreadyExistsExeption) {
      ExceptionBody error = new ExceptionBody(objectAlreadyExistsExeption.getMessage());
      Test test = testServiсe.getTestById(testId);
      List<Question> questions = questionServiсe.getQuestionsByTest(test);
      model.addAttribute("test", test);
      model.addAttribute("questions", questions);
      List<TelegramUser> telegramUsers = telegramUserServiсe.getTelegramUserByAuthenticationUser();
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
      Iterable<Question> questions = questionServiсe.getQuestionsByAuthenticationUser();
      model.addAttribute("questions", questions);
      return "testNew";
    }
    testServiсe.saveTest(test);
    return "redirect:/test";
  }

}
