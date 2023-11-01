package ru.fqw.TestingServis.bot.servise.bot;

import jakarta.ws.rs.ext.ParamConverter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
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


@Service
public class TelegramTestingServise {
    @Autowired
    TelegramUserServise telegramUserServise;
    @Qualifier("testingMapRepo") //Уточнение для возможности масштабирования
    @Autowired
    TestingRepo testingRepo;
    @Autowired
    ResultTestServise resultTestServise;
    @Autowired
    TelegramTestingHelper telegramTestingHelper;

    @Autowired
    public TelegramTestingServise(@Lazy TelegramBot telegramBot) { // используем ленивую подгрузку, чтобы избежать зацикленности
        this.telegramBot = telegramBot;
    }

    private TelegramBot telegramBot;

    public void testing(Update update) {

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
                    resultSave(testFromTelegramUser, message.getChatId());
                    testingRepo.delite(message.getChatId());
                    telegramBot.sendMessege(message.getChatId(), "Тест завершен");
                }
        }
    }
    @Scheduled(cron = "0 * * * * *") //Вызываем метод раз в минуту
    private void endTestByTime(){ // Пробегаем по всем активным тестам, и закрываем их если время на них вышло
       List<TestFromTelegramUser> testFromTelegramUsers = testingRepo.getAll();
       if (testFromTelegramUsers == null) return;
       for (TestFromTelegramUser testFromTelegramUser:testFromTelegramUsers) {
           if (testFromTelegramUser.getTimeStart().getTime() + (testFromTelegramUser.getTest().getTimeActiv()*60000L) > System.currentTimeMillis()) return;
            List<Long> chatIds = testingRepo.getChatIdsByTest(testFromTelegramUser);

           for (Long chatId:chatIds) {
                resultSave(testFromTelegramUser,chatId);
                testingRepo.delite(chatId);
                telegramBot.sendMessege(chatId, "Тест завершен автоматически, поскольку время на его прохождение вышло. Текущие результаты сохранены");
           }
        }
    }

    private void goNextQuestion(TelegramBot telegramBot, Message message, TestFromTelegramUser testFromTelegramUser, Question question) {
        telegramBot.sendMessege(message.getChatId(), question.toString());
        testFromTelegramUser.setCurrentQuestion(testFromTelegramUser.getCurrentQuestion() + 1);
    }

    private void resultSave(TestFromTelegramUser testFromTelegramUser, long chatId) {
        testFromTelegramUser.setTimeEnd(new Timestamp(System.currentTimeMillis()));
        ResultTest resultTest = new ResultTest();
        resultTest.setBall(testFromTelegramUser.getBall());
        resultTest.setTest(testFromTelegramUser.getTest());
        resultTest.setTelegramUser(telegramUserServise.getTelegramUserByChatId(chatId));
        resultTest.setAnswerFromTelegramUserList(testFromTelegramUser.getAnswerFromTelegramUserList());
        resultTest.setTimeStart(testFromTelegramUser.getTimeStart());
        resultTest.setTimeEnd(testFromTelegramUser.getTimeEnd());
        resultTestServise.save(resultTest);
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

    public void startTest(List<Long> tgUsersChatIds, Test test) {
        TestFromTelegramUser testFromTelegramUser = new TestFromTelegramUser(test);
        for (long chatId : tgUsersChatIds) {
            testingRepo.save(chatId, testFromTelegramUser);
            telegramBot.sendMessege(chatId, "Внимание!\nДля вас активирован следующий тест:\n" + test + "\nДля начала введите /go");
        }
    }

}
