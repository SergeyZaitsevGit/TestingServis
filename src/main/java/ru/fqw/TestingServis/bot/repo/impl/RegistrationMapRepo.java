package ru.fqw.TestingServis.bot.repo.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.bot.models.Cancellable;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.repo.RegistrationRepo;

@Component
public class RegistrationMapRepo implements RegistrationRepo, Cancellable {

  private final Map<Long, TelegramUser> registrationMap = new ConcurrentHashMap<>(); //Ключ - id чата в телеграмме

  @Override
  public void save(Long chatId, TelegramUser telegramUser) {
    registrationMap.put(chatId, telegramUser);
  }

  @Override
  public void delite(Long chatId) {
    registrationMap.remove(chatId);
  }

  @Override
  public TelegramUser get(Long chatId) {
    return registrationMap.get(chatId);
  }

  @Override
  public boolean isUserPassesRegistration(Long chatId) {
    return registrationMap.get(chatId) != null;
  }

  @Override
  public void cancel(Long chatId) {
    delite(chatId);
  }
}
