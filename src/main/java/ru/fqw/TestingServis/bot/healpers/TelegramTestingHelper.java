package ru.fqw.TestingServis.bot.healpers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;

@Component
public class TelegramTestingHelper {

  public int getBallByAnswers(BaseQuestion question, List<BaseAnswer> selectedAnswers) {
    switch (question.getTypeAnswerOptions()) {
      case ONE_ANSWER -> {
        if (selectedAnswers.get(0).isCorrected()) {
          return question.getBall();
        }
      }
      case MANY_ANSWER -> {
        int countCorrectAnswer = 0;
        for (BaseAnswer answer : question.getBaseAnswerList()) {
          if (answer.isCorrected()) {
            countCorrectAnswer++;
          }
        }
        final float ballBehindAnswer = (float) question.getBall() / countCorrectAnswer;
        float resultBall = 0;
        for (BaseAnswer answer : selectedAnswers) {
          if (answer.isCorrected()) {
            resultBall += ballBehindAnswer;
          } else {
            resultBall -= ballBehindAnswer;
          }
        }
        if (resultBall < 0) {
          resultBall = 0;
        }
        return Math.round(resultBall);
      }
    }
    return 0;
  }

  public List<Integer> getIntListByAnswer(String textAnswer) {
    List<Integer> convertedtextAnswerList = Stream.of(textAnswer.split(","))
        .map(String::trim)
        .map(Integer::parseInt)
        .collect(Collectors.toList());
    return convertedtextAnswerList;
  }

}
