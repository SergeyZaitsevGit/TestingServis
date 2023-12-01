package ru.fqw.TestingServis.bot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerWhithSelect extends BaseAnswer {

  boolean selected = false;

  public AnswerWhithSelect(BaseAnswer baseAnswer, boolean selected) {
    super(baseAnswer);
    this.selected = selected;
  }
}