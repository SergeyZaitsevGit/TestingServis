package ru.fqw.TestingServis.bot.repo;

import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestingMapRepo implements TestingRepo{
private Map<Long, TestFromTelegramUser> testMap = new HashMap<>();
    @Override
    public void save(Long chatId, TestFromTelegramUser testFromTelegramUser) {
        testMap.put(chatId,testFromTelegramUser);
    }

    @Override
    public void delite(Long chatId) {
        testMap.remove(chatId);
    }

    @Override
    public TestFromTelegramUser get(Long chatId) {
        return testMap.get(chatId);
    }

    @Override
    public boolean isUserHaveTest(Long chatId) {
        return testMap.get(chatId) != null;
    }
}
