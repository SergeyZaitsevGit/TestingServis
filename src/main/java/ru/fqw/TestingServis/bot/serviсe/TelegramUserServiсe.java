package ru.fqw.TestingServis.bot.serviсe;

import java.util.List;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;

public interface TelegramUserServiсe {
  TelegramUser saveTelegramUser(TelegramUser telegramUser);
  List<TelegramUser> getTelegramUserByAuthenticationUser();
  boolean telegramUserExistsByChatId(long chatId);
  TelegramUser getTelegramUserByChatId(long chatId);
}
