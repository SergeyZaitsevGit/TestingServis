package ru.fqw.TestingServis.bot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;
import ru.fqw.TestingServis.site.models.question.Question;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class  AnswerFromTelegramUser {
    private BaseQuestion question;
    private String freeAnswer;
    private List<BaseAnswer> answers;
    private int ballBehindQuestion;

    public AnswerFromTelegramUser(BaseQuestion question, List<BaseAnswer> answers, int ballBehindQuestion) {
        this.question = question;
        this.answers = answers;
        this.ballBehindQuestion = ballBehindQuestion;
    }

    public AnswerFromTelegramUser(BaseQuestion question, String freeAnswer, int ballBehindQuestion) {
        this.question = question;
        this.freeAnswer = freeAnswer;
        this.ballBehindQuestion = ballBehindQuestion;
    }
}
