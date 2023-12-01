package ru.fqw.TestingServis.site.models.question;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.emuns.TypeAnswerOptions;

import java.util.ArrayList;
import java.util.List;

@Data
@MappedSuperclass
@NoArgsConstructor
public class BaseQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Вопрос не может быть пустым")
  protected String text;

  @Min(value = 1, message = "Ошибка ввода балла")
  protected int ball;
  protected TypeAnswerOptions typeAnswerOptions = TypeAnswerOptions.ONE_ANSWER;

  @Transient
  protected List<BaseAnswer> baseAnswerList = new ArrayList<>();

  public BaseQuestion(BaseQuestion baseQuestion) {
    id = baseQuestion.getId();
    text = baseQuestion.getText();
    ball = baseQuestion.getBall();
    typeAnswerOptions = baseQuestion.getTypeAnswerOptions();
    baseAnswerList.addAll(baseQuestion.getBaseAnswerList());
  }

  public BaseQuestion(String text) {
    this.text = text;
  }
}
