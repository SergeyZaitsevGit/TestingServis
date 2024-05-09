package ru.fqw.TestingServis.bot.repo;

import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;

public interface RegistrationRepo {

  void save(Long chatId, TelegramUser telegramUser);

  void delite(Long chatId);

  TelegramUser get(Long chatId);

  boolean isUserPassesRegistration(Long chatId);
}
