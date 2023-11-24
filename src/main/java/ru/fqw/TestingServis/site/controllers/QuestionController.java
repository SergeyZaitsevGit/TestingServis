package ru.fqw.TestingServis.site.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.service.QuestionServiсe;
import ru.fqw.TestingServis.site.service.TypeServiсe;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class QuestionController {

  final QuestionServiсe questionServiсe;
  final TypeServiсe typeServiсe;

  @GetMapping("/question")
  public String questions(Model model) {
    Iterable<Question> questions = questionServiсe.getQuestionsByAuthenticationUser();
    Iterable<Type> types = typeServiсe.getTypeByAuthenticationUser();
    model.addAttribute("types", types);
    model.addAttribute("questions", questions);
    long questionId = -1;
    model.addAttribute("idQuestionDelete", questionId);

    return "question";
  }

}
