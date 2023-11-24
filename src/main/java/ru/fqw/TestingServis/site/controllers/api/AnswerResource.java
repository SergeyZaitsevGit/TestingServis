package ru.fqw.TestingServis.site.controllers.api;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fqw.TestingServis.site.service.AnswerService;

@RestController
@AllArgsConstructor
@RequestMapping("${apiUrl}/answer")
public class AnswerResource {

  AnswerService answerService;

  @DeleteMapping("/delete/{answerId}")
  @PreAuthorize("@customSecurityExpression.canAccessAnswer(#answerId)")
  public void dellAnswerById(@PathVariable long answerId) {
    answerService.delAnswerById(answerId);
  }
}
