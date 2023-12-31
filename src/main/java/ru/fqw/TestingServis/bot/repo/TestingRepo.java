package ru.fqw.TestingServis.bot.repo;

import java.util.List;
import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;

public interface TestingRepo {

  void save(Long chatId, TestFromTelegramUser testFromTelegramUser);

  void delite(Long chatId);

  TestFromTelegramUser get(Long chatId);

  boolean isUserHaveTest(Long chatId);

  List<TestFromTelegramUser> getAll();

  List<Long> getChatIdsByTest(TestFromTelegramUser test);

}
