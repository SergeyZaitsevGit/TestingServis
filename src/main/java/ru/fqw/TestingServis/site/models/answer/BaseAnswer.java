package ru.fqw.TestingServis.site.models.answer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseAnswer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotBlank(message = "Введите вариант ответа")
  protected String text;
  protected boolean corrected;
}
