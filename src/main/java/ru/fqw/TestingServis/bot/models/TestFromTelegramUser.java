package ru.fqw.TestingServis.bot.models;

import lombok.Data;
import ru.fqw.TestingServis.site.models.Answer;
import ru.fqw.TestingServis.site.models.Question;
import ru.fqw.TestingServis.site.models.Test;

import java.sql.Timestamp;
import java.util.Map;

@Data
public class TestFromTelegramUser {

    private Test test;
    private int currentQuestion;

    private Map<Question, Answer> questionAnswerMap;
    private int ball;

    private Timestamp timeStart;

    private Timestamp timeEnd;
}
