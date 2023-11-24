package ru.fqw.TestingServis.bot.service;

import java.util.List;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;

public interface TelegramUserService {
  TelegramUser saveTelegramUser(TelegramUser telegramUser);
  List<TelegramUser> getTelegramUserByAuthenticationUser();
  boolean telegramUserExistsByChatId(long chatId);
  TelegramUser getTelegramUserByChatId(long chatId);
}
