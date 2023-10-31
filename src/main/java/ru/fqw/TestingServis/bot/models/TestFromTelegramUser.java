package ru.fqw.TestingServis.bot.models;

import lombok.Data;
import ru.fqw.TestingServis.site.models.test.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class TestFromTelegramUser {

    private Test test;
    private int currentQuestion;

    private List<AnswerFromTelegramUser> answerFromTelegramUserList = new ArrayList<>();
    private int ball;

    private Timestamp timeStart;

    private Timestamp timeEnd;
    public TestFromTelegramUser(Test test) {
        this.test = test;
    }
}
