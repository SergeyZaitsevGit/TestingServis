package ru.fqw.TestingServis.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.healpers.TelegramTestingHelper;
import ru.fqw.TestingServis.bot.models.AnswerFromTelegramUser;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;
import ru.fqw.TestingServis.bot.repo.TestingRepo;
import ru.fqw.TestingServis.bot.service.ResultTestService;
import ru.fqw.TestingServis.bot.service.TelegramTestingService;
import ru.fqw.TestingServis.bot.service.TelegramUserService;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.exception.ObjectAlreadyExistsExeption;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.service.TestService;

import java.sql.Timestamp;
import java.util.*;


@Service
public class TelegramTestingServiceImpl implements TelegramTestingService {

  @Autowired
  TelegramUserService telegramUserService;
  @Qualifier("testingMapRepo") //Уточнение для возможности масштабирования
  @Autowired
  TestingRepo testingRepo;
  @Autowired
  ResultTestService resultTestService;
  @Autowired
  TelegramTestingHelper telegramTestingHelper;

  @Autowired
  TestService testService;

  @Autowired
  public TelegramTestingServiceImpl(
      @Lazy TelegramBot telegramBot) { // используем ленивую подгрузку, чтобы избежать зацикленности
    this.telegramBot = telegramBot;
  }

  private final TelegramBot telegramBot;
  @Override
  public void testing(Update update) { //Обработка прохождения теста

    Message message = update.getMessage();
    boolean isUserHaveTest = testingRepo.isUserHaveTest(message.getChatId());
    if (isUserHaveTest) {
      TestFromTelegramUser testFromTelegramUser = testingRepo.get(
          message.getChatId());                           //Получаем тест с дополнительными данными
      List<Question> questionList = new ArrayList<>(testFromTelegramUser.getTest()
          .getQuestionSet());             //Список вопросов полученного теста
      boolean isTestDontStart = testFromTelegramUser.getTimeStart() == null;
      if (isTestDontStart) { // Eсли тест не начат предлагаем пользователю его начать
        if (message.getText().equals("/go")) {
          if (testFromTelegramUser.getTest().isMixQuestions()) {
            Collections.shuffle(questionList);
            testFromTelegramUser.getTest().setQuestionSet(new LinkedHashSet<>(questionList));
          }
          testFromTelegramUser.setTimeStart(new Timestamp(System.currentTimeMillis()));
          Question question = questionList.get(testFromTelegramUser.getCurrentQuestion());
          if (testFromTelegramUser.getTest().isMixAnswers()) {
            Collections.shuffle(question.getAnswerList());
          }
          telegramBot.sendMessege(
              message.getChatId(), "Вы начали тестирование\n"
                  + question
          );
          testFromTelegramUser.setCurrentQuestion(testFromTelegramUser.getCurrentQuestion() + 1);
          return;
        }
        telegramBot.sendMessege(message.getChatId(),
            "Для начала тестирования введите комманду /go");
      } else if (testFromTelegramUser.getCurrentQuestion() < testFromTelegramUser.getTest()
          .getQuestionSet().size()) { // Пока вопросы не кончатся выводим их и фиксируем ответ
        try {
          Question question = questionList.get(testFromTelegramUser.getCurrentQuestion());
          parseAnswer(testFromTelegramUser, questionList, message);
          if (testFromTelegramUser.getTest().isMixAnswers()) {
            Collections.shuffle(question.getAnswerList());
          }
          goNextQuestion(telegramBot, message, testFromTelegramUser, question);
        } catch (NumberFormatException e) {
          telegramBot.sendMessege(message.getChatId(), "Неверный формат ввода. повторите попытку.");
        } catch (IndexOutOfBoundsException e) {
          telegramBot.sendMessege(message.getChatId(), "Вы введи несуществующий вариант ответа.");
        }

      } else if (testFromTelegramUser.getCurrentQuestion() == testFromTelegramUser.getTest()
          .getQuestionSet()
          .size()) { // Когда вопросы кончились фиксируем последний ответ и сохраняем тест
        try {
          parseAnswer(testFromTelegramUser, questionList, message);
          resultSave(testFromTelegramUser, message.getChatId());
          testingRepo.delite(message.getChatId());
          telegramBot.sendMessege(message.getChatId(), "Тест завершен");
          if (testingRepo.getChatIdsByTest(testFromTelegramUser).isEmpty()) {
            Test test = testFromTelegramUser.getTest();
            testService.updateTestActivById(test.getId(), false);
          }
        } catch (NumberFormatException e) {
          telegramBot.sendMessege(message.getChatId(), "Неверный формат ввода. Повторите попытку.");
        } catch (IndexOutOfBoundsException e) {
          telegramBot.sendMessege(message.getChatId(), "Вы введи несуществующий вариант ответа.");
        }
      }
    }
  }

  @Override
  public void startTest(List<Long> tgUsersChatIds, Test test, String title) {
    if (resultTestService.existByTitle(title)) {
      throw new ObjectAlreadyExistsExeption("Тестирование с таким названием уже существует");
    }
    for (long chatId : tgUsersChatIds) {
      if (testingRepo.isUserHaveTest(chatId)) {
        continue;
      }
      testingRepo.save(chatId, new TestFromTelegramUser(new Test(test), title));
      telegramBot.sendMessege(
          chatId,
          "Внимание!\nДля вас активирован следующий тест:\n" + test + "\nДля начала введите /go"
      );
    }
  }

  @Scheduled(cron = "0 * * * * *")
  // Вызываем метод раз в минуту
  private void endTestByTime() {                                                                                      // Пробегаем по всем активным тестам, и закрываем их если время на них вышло
    List<TestFromTelegramUser> testsFromTelegramUsers = testingRepo.getAll();
    if (testsFromTelegramUsers == null) {
      return;
    }
    for (TestFromTelegramUser testFromTelegramUser : testsFromTelegramUsers) {
      if (testFromTelegramUser.getTimeStart() == null) {
        return;
      }
      long timeStart = testFromTelegramUser.getTimeStart().getTime();
      long timeEnd = (testFromTelegramUser.getTest().getTimeActiv() * 60000L);
      if (timeStart + timeEnd > System.currentTimeMillis()) {
        return;                                              //Проверка не вышло ли время теста
      }
      Test test = testFromTelegramUser.getTest();
      testService.updateTestActivById(test.getId(), false);
      List<Long> chatIds = testingRepo.getChatIdsByTest(testFromTelegramUser);
      for (Long chatId : chatIds) {
        resultSave(testFromTelegramUser, chatId);
        testingRepo.delite(chatId);
        telegramBot.sendMessege(chatId,
            "Тест завершен автоматически, поскольку время на его прохождение вышло. Текущие результаты сохранены");
      }
    }
  }

  private void goNextQuestion(TelegramBot telegramBot,
      Message message,
      TestFromTelegramUser testFromTelegramUser,
      Question question) {
    telegramBot.sendMessege(message.getChatId(), question.toString());
    testFromTelegramUser.setCurrentQuestion(testFromTelegramUser.getCurrentQuestion() + 1);
  }

  private void resultSave(TestFromTelegramUser testFromTelegramUser, long chatId) {
    testFromTelegramUser.setTimeEnd(new Timestamp(System.currentTimeMillis()));
    Test test = testFromTelegramUser.getTest();
    ResultTest resultTest = new ResultTest();
    resultTest.setBall(testFromTelegramUser.getBall());
    resultTest.setTest(test);
    resultTest.setTelegramUser(telegramUserService.getTelegramUserByChatId(chatId));
    resultTest.setAnswerFromTelegramUserList(testFromTelegramUser.getAnswerFromTelegramUserList());
    resultTest.setTimeStart(testFromTelegramUser.getTimeStart());
    resultTest.setTimeEnd(testFromTelegramUser.getTimeEnd());
    resultTest.setTitle(testFromTelegramUser.getTitle());
    resultTestService.saveResult(resultTest);
  }

  private void parseAnswer(TestFromTelegramUser testFromTelegramUser,
      List<Question> questionList, Message message) {
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
        int ballBehindQuestion = telegramTestingHelper.getBallByAnswers(lastQuestion,
            answersSelected);
        testFromTelegramUser.setBall(testFromTelegramUser.getBall() + ballBehindQuestion);
        testFromTelegramUser.getAnswerFromTelegramUserList()
            .add(new AnswerFromTelegramUser(lastQuestion, answersSelected, ballBehindQuestion));
      }
      case ONE_ANSWER -> {
        int answerIndex = Integer.parseInt(message.getText());
        List<BaseAnswer> answers = new ArrayList<>();
        answers.add(lastQuestion.getAnswerList().get(answerIndex - 1));
        int ballBehindQuestion = telegramTestingHelper.getBallByAnswers(lastQuestion, answers);
        testFromTelegramUser.setBall(testFromTelegramUser.getBall() + ballBehindQuestion);
        testFromTelegramUser.getAnswerFromTelegramUserList()
            .add(new AnswerFromTelegramUser(lastQuestion, answers, ballBehindQuestion));
      }
      case FREE_ANSWER -> {
        testFromTelegramUser.getAnswerFromTelegramUserList()
            .add(new AnswerFromTelegramUser(lastQuestion, message.getText(), 0));
      }
    }
  }


}
