package ru.fqw.TestingServis.bot.repo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.bot.models.Cancellable;
import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;
import ru.fqw.TestingServis.bot.repo.TestingRepo;

@Component
public class TestingMapRepo implements TestingRepo, Cancellable {

  private Map<Long, TestFromTelegramUser> testMap = new ConcurrentHashMap<>();

  @Override
  public void save(Long chatId, TestFromTelegramUser testFromTelegramUser) {
    testMap.put(chatId, testFromTelegramUser);
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

  @Override
  public List<TestFromTelegramUser> getAll() {
    return new ArrayList<>(testMap.values());
  }

  @Override
  public List<Long> getChatIdsByTest(TestFromTelegramUser test) {
    return testMap
        .entrySet()
        .stream()
        .filter(entry -> test.equals(entry.getValue()))
        .map(Map.Entry::getKey).collect(Collectors.toList());
  }

  @Override
  public void cancel(Long chatId) {
    delite(chatId);
  }
}
