package ru.fqw.TestingServis.site.models.question;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.emuns.TypeAnswerOptions;

import java.util.ArrayList;
import java.util.List;

@Data
@MappedSuperclass
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
}
