package ru.fqw.TestingServis.site.controllers.api;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.service.AnswerService;
import ru.fqw.TestingServis.site.service.QuestionService;
import ru.fqw.TestingServis.site.service.TypeService;
import ru.fqw.TestingServis.site.service.UserService;

@RestController
@RequestMapping("${apiUrl}/question")
@AllArgsConstructor
public class QuestionResource {

  QuestionService questionService;
  AnswerService answerService;
  TypeService typeService;
  UserService userService;

  @PostMapping
  @PreAuthorize("@customSecurityExpression.canAccessType(#typeId)")
  public Question createQuestion(@RequestBody Question question,
      @RequestParam("typeId") Long typeId) {

    if (typeId != -1) {
      question.setType(typeService.getTypeById(typeId));
    }
    List<Answer> answerList = new ArrayList<>(question.getAnswerList());
    question.getAnswerList().clear();
    if (question.getId() == null) {
      questionService.saveQuestion(question);
    } else {
      questionService.updateQuestion(question);
    }

    for (Answer a : answerList) {
      answerService.saveAnswer(a, question);
    }
    return question;
  }

  @GetMapping("type/{typeId}")
  @PreAuthorize("@customSecurityExpression.canAccessType(#typeId)")
  public List<Long> findQuestionByTypeId(
      @PathVariable long typeId) {                                                 //Получение списка id вопросов, соответствующих выбранному типу
    List<Question> questions = questionService.getQuestionsByType(typeService.getTypeById(typeId));
    List<Long> questionIds = new ArrayList<>();
    for (Question question : questions) {
      questionIds.add(question.getId());
    }

    return questionIds;
  }

  @GetMapping("/{questionId}")
  @PreAuthorize("@customSecurityExpression.canAccessQuestion(#questionId)")
  public Question findTypeById(@PathVariable long questionId) {
    return questionService.getQuestionById(questionId);
  }

  @PreAuthorize("@customSecurityExpression.canAccessQuestion(#id)")
  @DeleteMapping("/delete/{id}")
  public void deleteQuestion(@PathVariable long id) {
    questionService.deliteQuestionById(id);
  }
}
