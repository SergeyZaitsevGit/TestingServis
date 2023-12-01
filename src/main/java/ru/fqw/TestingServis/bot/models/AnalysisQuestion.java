package ru.fqw.TestingServis.bot.models;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;

//TODO фабрицный метод
@Data
public class AnalysisQuestion {

  private BigDecimal percentCorrected;

  private BigDecimal avgBall;

  private List<BaseAnswer> mostFrequentAnswers;

  private final BaseQuestion baseQuestion;

  public AnalysisQuestion(BaseQuestion baseQuestion) {
    this.baseQuestion = baseQuestion;
  }
}
