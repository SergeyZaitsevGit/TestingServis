package ru.fqw.TestingServis.bot.servise.bot;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.healpers.TelegramTestingHelper;
import ru.fqw.TestingServis.bot.models.AnswerFromTelegramUser;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;
import ru.fqw.TestingServis.bot.repo.TestingRepo;
import ru.fqw.TestingServis.bot.servise.ResultTestServise;
import ru.fqw.TestingServis.bot.servise.TelegramUserServise;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
public class TelegramTestingServise {
    TelegramUserServise telegramUserServise;
    @Qualifier("testingMapRepo")
    TestingRepo testingRepo;
    ResultTestServise resultTestServise;
    TelegramTestingHelper telegramTestingHelper;

    public void testing(Update update, TelegramBot telegramBot) {

        Message message = update.getMessage();
        boolean isUserHaveTest = testingRepo.isUserHaveTest(message.getChatId());
        if (isUserHaveTest) {
            TestFromTelegramUser testFromTelegramUser = testingRepo.get(message.getChatId()); //Получаем тест с дополнительными данными
            List<Question> questionList = new ArrayList<>();
            questionList.addAll(testFromTelegramUser.getTest().getQuestionSet()); //Список вопросов полученного теста
            boolean isTestDontStart = testFromTelegramUser.getTimeStart() == null;
                if (isTestDontStart) {
                    if (message.getText().equals("/go")) {
                        testFromTelegramUser.setTimeStart(new Timestamp(System.currentTimeMillis()));
                        telegramBot.sendMessege(
                                message.getChatId(), "Вы начали тестирование\n"
                                        + questionList.get(testFromTelegramUser.getCurrentQuestion())
                        );
                        testFromTelegramUser.setCurrentQuestion(testFromTelegramUser.getCurrentQuestion() + 1);
                        return;
                    }
                    telegramBot.sendMessege(message.getChatId(), "Для начала тестирования введите комманду /go");
                }
                else if (testFromTelegramUser.getCurrentQuestion() < testFromTelegramUser.getTest().getQuestionSet().size()) {
                    try {
                        parseAnswer(testFromTelegramUser, questionList, telegramBot, message);
                        goNextQuestion(telegramBot, message, testFromTelegramUser, questionList.get(testFromTelegramUser.getCurrentQuestion()));
                    } catch (NumberFormatException e) {
                        telegramBot.sendMessege(message.getChatId(), "Неверный формат ввода. повторите попытку.");
                    } catch (IndexOutOfBoundsException e) {
                        telegramBot.sendMessege(message.getChatId(), "Вы введи несуществующий вариант ответа.");
                    }

                }
                else {
                    parseAnswer(testFromTelegramUser, questionList, telegramBot, message);
                    resultSave(testFromTelegramUser, message);
                    testingRepo.delite(message.getChatId());
                }
        }
    }


    private void resultSave(TestFromTelegramUser testFromTelegramUser, Message message) {
        ResultTest resultTest = new ResultTest();
        resultTest.setBall(testFromTelegramUser.getBall());
        resultTest.setTest(testFromTelegramUser.getTest());
        resultTest.setTelegramUser(telegramUserServise.getTelegramUserByChatId(message.getChatId()));
        resultTest.setAnswerFromTelegramUserList(testFromTelegramUser.getAnswerFromTelegramUserList());
        resultTest.setTimeStart(testFromTelegramUser.getTimeStart());
        resultTest.setTimeEnd(testFromTelegramUser.getTimeEnd());
        resultTestServise.save(resultTest);
    }

    private void goNextQuestion(TelegramBot telegramBot, Message message, TestFromTelegramUser testFromTelegramUser, Question question) {
        telegramBot.sendMessege(message.getChatId(), question.toString());
        testFromTelegramUser.setCurrentQuestion(testFromTelegramUser.getCurrentQuestion() + 1);
    }

    private void parseAnswer(TestFromTelegramUser testFromTelegramUser, List<Question> questionList, TelegramBot telegramBot, Message message) {
        int currentQuestionIndex = testFromTelegramUser.getCurrentQuestion();
        Question lastQuestion = questionList.get(currentQuestionIndex - 1);
            switch (lastQuestion.getTypeAnswerOptions()) {
                case MANY_ANSWER -> {
                    List<Integer> listAnswersInt = telegramTestingHelper.getIntListByAnswer(message.getText());
                    List<BaseAnswer> answersSelected = new ArrayList<>();
                        for (int answerIndex : listAnswersInt) {
                            Answer answer = lastQuestion.getAnswerList().get(answerIndex - 1);
                            answersSelected.add(answer);
                        }
                    int ballBehindQuestion = telegramTestingHelper.getBallByAnswers(lastQuestion,answersSelected);
                    testFromTelegramUser.setBall(testFromTelegramUser.getBall() + ballBehindQuestion);
                    testFromTelegramUser.getAnswerFromTelegramUserList().add(new AnswerFromTelegramUser(lastQuestion, answersSelected, ballBehindQuestion));
                }
                case ONE_ANSWER -> {
                    int answerIndex = Integer.parseInt(message.getText());
                    List<BaseAnswer> answers = new ArrayList<>();
                    answers.add(lastQuestion.getAnswerList().get(answerIndex - 1));
                    int ballBehindQuestion = telegramTestingHelper.getBallByAnswers(lastQuestion,answers);
                    testFromTelegramUser.setBall(testFromTelegramUser.getBall() + ballBehindQuestion);
                    testFromTelegramUser.getAnswerFromTelegramUserList().add(new AnswerFromTelegramUser(lastQuestion, answers,ballBehindQuestion));
                }
                case FREE_ANSWER -> {
                    testFromTelegramUser.getAnswerFromTelegramUserList().add(new AnswerFromTelegramUser(lastQuestion, message.getText(), 0));
                }
            }
    }

    public void startTest(List<Long> tgUsersChatIds, Test test, TelegramBot telegramBot) {
        TestFromTelegramUser testFromTelegramUser = new TestFromTelegramUser(test);
        for (long chatId : tgUsersChatIds) {
            testingRepo.save(chatId, testFromTelegramUser);
            telegramBot.sendMessege(chatId, "Внимание!\nДля вас активирован следующий тест:\n" + test + "\nДля начала введите /go");
        }
    }

}
