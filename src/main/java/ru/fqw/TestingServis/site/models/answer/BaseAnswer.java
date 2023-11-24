package ru.fqw.TestingServis.site.models.answer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotBlank(message = "Введите вариант ответа")
  protected String text;
  protected boolean corrected;

  public BaseAnswer(BaseAnswer baseAnswer) {
    id = baseAnswer.getId();
    text = baseAnswer.getText();
    corrected = baseAnswer.isCorrected();
  }
}
