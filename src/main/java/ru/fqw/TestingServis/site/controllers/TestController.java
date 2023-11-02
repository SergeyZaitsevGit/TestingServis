package ru.fqw.TestingServis.site.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.servise.TelegramUserServise;
import ru.fqw.TestingServis.bot.servise.bot.TelegramTestingServise;
import ru.fqw.TestingServis.site.models.*;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.servise.AnswerServise;
import ru.fqw.TestingServis.site.servise.QuestionServise;
import ru.fqw.TestingServis.site.servise.TestServise;
import ru.fqw.TestingServis.site.servise.TypeServise;

import java.util.List;

@RequestMapping
@Controller
@AllArgsConstructor
public class TestController {
    TestServise testServise;
    QuestionServise questionServise;
    AnswerServise answerServise;
    TypeServise typeServis;
    TelegramUserServise telegramUserServise;
    TelegramTestingServise testingServise;

    @GetMapping("/test")
    public String tests(Model model) {
        Iterable<Test> tests = testServise.getTestsByAuthenticationUser();
        model.addAttribute("tests", tests);
        return "test";
    }

    @PreAuthorize("@customSecurityExpression.canAccessTest(#testId)")
    @GetMapping("/test/{testId}")
    public String testCurred(@PathVariable Long testId, Model model) {
        Test test = testServise.getTestById(testId);
        List<Question> questions = questionServise.getQuestionsByTest(test);
        model.addAttribute("test", test);
        model.addAttribute("questions", questions);
        List<TelegramUser> telegramUsers = telegramUserServise.getTelegramUserByAuthenticationUser();
        model.addAttribute("telegramUsers", telegramUsers);
        return "testCurred";
    }

    @GetMapping("/test/new")
    public String newTest(Model model) {
        model.addAttribute("test", new Test());
        model.addAttribute("question", new Question());
        List<Question> questions = questionServise.getQuestionsByAuthenticationUser();
        List<Type> types = typeServis.getTypeByAuthenticationUser();
        model.addAttribute("types", types);
        model.addAttribute("questions", questions);
        return "testNew";
    }

    @GetMapping("/test/edit/{testId}")
    @PreAuthorize("@customSecurityExpression.canAccessTest(#testId)")
    public String testEdit(@PathVariable Long testId, Model model) {
        Test test = testServise.getTestById(testId);
        List<Question> questions = questionServise.getQuestionsByAuthenticationUser();
        List<Type> types = typeServis.getTypeByAuthenticationUser();
        model.addAttribute("test", test);
        model.addAttribute("questions", questions);
        model.addAttribute("types", types);
        return "testEdit";
    }

    @PreAuthorize("@customSecurityExpression.canAccessTelegramUser(#tgUsersResult)")
    @PostMapping("/test/{testId}")
    public String testCurred(@PathVariable Long testId, @RequestParam("checked") List<Long> tgUsersResult, Model model) {
        Test test = testServise.getTestById(testId);
        testingServise.startTest(tgUsersResult,test);
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
            Iterable<Question> questions = questionServise.getQuestionsByAuthenticationUser();
            model.addAttribute("questions", questions);
            return "testNew";
        }
        testServise.saveTest(test);
        return "redirect:/test";
    }

}
