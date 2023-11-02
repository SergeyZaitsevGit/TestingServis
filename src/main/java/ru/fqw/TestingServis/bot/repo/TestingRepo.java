package ru.fqw.TestingServis.bot.repo;

import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;

import java.util.List;

public interface TestingRepo {
    void save(Long chatId, TestFromTelegramUser testFromTelegramUser);
    void delite(Long chatId);
    TestFromTelegramUser get(Long chatId);
    boolean isUserHaveTest(Long chatId);
    List<TestFromTelegramUser> getAll();
    List<Long> getChatIdsByTest(TestFromTelegramUser test);

}
