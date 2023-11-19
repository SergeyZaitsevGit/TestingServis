package ru.fqw.TestingServis.bot.repo;

import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import java.util.HashMap;
import java.util.Map;

@Component
public class RegistrationMapRepo implements RegistrationRepo {

  private final Map<Long, TelegramUser> registrationMap = new HashMap<>(); //Ключ - id чата в телеграмме

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
  public boolean isUserpassesRegistration(Long chatId) {
    return registrationMap.get(chatId) != null;
  }
}
