package ru.fqw.TestingServis.bot.serviсe.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.repo.TelegramUserRepo;
import ru.fqw.TestingServis.bot.serviсe.TelegramUserServiсe;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.service.UserServiсe;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUserServiсeImpl implements TelegramUserServiсe {

  final TelegramUserRepo telegramUserRepo;
  final UserServiсe userServiсe;

  public TelegramUser saveTelegramUser(TelegramUser telegramUser) {
    return telegramUserRepo.save(telegramUser);
  }

  public List<TelegramUser> getTelegramUserByAuthenticationUser() {
    return telegramUserRepo.findByuserSetInvited(userServiсe.getAuthenticationUser());
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
