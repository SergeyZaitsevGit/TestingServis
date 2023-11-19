package ru.fqw.TestingServis.site.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.servise.QuestionServise;
import ru.fqw.TestingServis.site.servise.TypeServise;

@Controller
@RequestMapping
@AllArgsConstructor
public class QuestionController {

  QuestionServise questionServise;
  TypeServise typeServise;

  @GetMapping("/question")
  public String questions(Model model) {
    Iterable<Question> questions = questionServise.getQuestionsByAuthenticationUser();
    Iterable<Type> types = typeServise.getTypeByAuthenticationUser();
    model.addAttribute("types", types);
    model.addAttribute("questions", questions);
    long questionId = -1;
    model.addAttribute("idQuestionDelete", questionId);

    return "question";
  }

}
