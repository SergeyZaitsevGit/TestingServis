package ru.fqw.TestingServis.bot.models;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AnswerFromTelegramUser {

  private BaseQuestion question;
  private String freeAnswer;
  private List<BaseAnswer> answers;
  private int ballBehindQuestion;

  public AnswerFromTelegramUser(BaseQuestion question, List<BaseAnswer> answers,
      int ballBehindQuestion) {
    this.question = question;
    this.answers = answers;
    this.ballBehindQuestion = ballBehindQuestion;
  }

  public AnswerFromTelegramUser(BaseQuestion question, String freeAnswer, int ballBehindQuestion) {
    this.question = question;
    this.freeAnswer = freeAnswer;
    this.ballBehindQuestion = ballBehindQuestion;
  }

  public List<AnswerWhithSelect> getAnswerWhithSelect() {
    List<AnswerWhithSelect> result = new ArrayList<>();
    for (BaseAnswer answer : question.getBaseAnswerList()) {
      result.add(new AnswerWhithSelect(answer, answers.contains(answer)));
    }
    return result;
  }

}


