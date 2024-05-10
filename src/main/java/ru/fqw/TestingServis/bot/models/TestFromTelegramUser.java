package ru.fqw.TestingServis.bot.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;
import ru.fqw.TestingServis.site.models.test.Test;

@Data
public class TestFromTelegramUser {

  private Test test;

  private int currentQuestion;
  private int ball;

  private List<AnswerFromTelegramUser> answerFromTelegramUserList = new ArrayList<>();
  private List<BaseQuestion> questions = new ArrayList<>();
  private Timestamp timeStart;
  private Timestamp timeEnd;
  private Timestamp timeActiv;

  private String title;

  public TestFromTelegramUser(Test test, String title) {

    this.test = test;
    this.title = title;
  }

}
