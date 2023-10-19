package ru.fqw.TestingServis.site.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.models.*;
import ru.fqw.TestingServis.site.repo.QuestionRepo;
import ru.fqw.TestingServis.site.repo.UserRepository;
import ru.fqw.TestingServis.site.servise.AnswerServise;
import ru.fqw.TestingServis.site.servise.QuestionServise;
import ru.fqw.TestingServis.site.servise.TestServise;
import ru.fqw.TestingServis.site.servise.TypeServise;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RequestMapping
@Controller
@AllArgsConstructor
public class TestController {
    TestServise testServise;
    QuestionServise questionServise;
    AnswerServise answerServise;
    TypeServise typeServis;
    @GetMapping("/test")
    public String tests(Model model) {
        Iterable<Test> tests = testServise.getTestsByAuthenticationUser();
        model.addAttribute("tests",tests);
        return "test";
    }

    @GetMapping("/test/{testId}")
    public String test( @PathVariable Long testId, Model model) {
        Test test = testServise.getTestById(testId);
        List<Question> questions = questionServise.getQuestionsByTest(test);
        model.addAttribute("test", test);
        model.addAttribute("questions", questions);
        return "testCurred";
    }

    @GetMapping("/test/new")
    public String newTest(Model model) {
        model.addAttribute("test", new Test());
        model.addAttribute("question", new Question());
        Iterable<Question> questions =questionServise.getQuestionsByAuthenticationUser();
        Iterable<Type> types = typeServis.getTypeByAuthenticationUser();
        model.addAttribute("types", types);
        model.addAttribute("questions",questions);
        return "testNew";
    }

    @PostMapping("/test/new")
    public String newTest(
            @ModelAttribute("test")
            @Valid Test test, Errors errors,
            Model model
    ){
        if (errors.hasErrors()) {
            model.addAttribute("question", new Question());
            Iterable<Question> questions =questionServise.getQuestionsByAuthenticationUser();
            model.addAttribute("questions",questions);
            return "test-new";
        };
        testServise.saveTest(test);
        return "redirect:/test";
    }

}
