package ru.fqw.TestingServis.site.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.service.QuestionService;
import ru.fqw.TestingServis.site.service.TypeService;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class QuestionController {

  final QuestionService questionService;
  final TypeService typeService;

  @GetMapping("/question")
  public String questions(Model model) {
    Iterable<Question> questions = questionService.getQuestionsByAuthenticationUser();
    Iterable<Type> types = typeService.getTypeByAuthenticationUser();
    model.addAttribute("types", types);
    model.addAttribute("questions", questions);
    long questionId = -1;
    model.addAttribute("idQuestionDelete", questionId);

    return "question";
  }

}
