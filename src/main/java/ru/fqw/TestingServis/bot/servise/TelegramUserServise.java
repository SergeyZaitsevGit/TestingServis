package ru.fqw.TestingServis.bot.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.repo.TelegramUserRepo;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.servise.UserServise;

import java.util.List;

@Service
@AllArgsConstructor
public class TelegramUserServise {

  TelegramUserRepo telegramUserRepo;
  UserServise userServise;

  public TelegramUser saveTelegramUser(TelegramUser telegramUser) {
    return telegramUserRepo.save(telegramUser);
  }

  public List<TelegramUser> getTelegramUserByAuthenticationUser() {
    return telegramUserRepo.findByuserSetInvited(userServise.getAuthenticationUser());
  }

  public boolean telegramUserExistsByChatId(long chatId) {
    return telegramUserRepo.existsByChatId(chatId);
  }

  public TelegramUser getTelegramUserByChatId(long chatId) {
    return telegramUserRepo.getTelegramUserByChatId(chatId).orElseThrow(
        () -> new ResourceNotFoundException("TelegramUser not found")
    );
  }
}
