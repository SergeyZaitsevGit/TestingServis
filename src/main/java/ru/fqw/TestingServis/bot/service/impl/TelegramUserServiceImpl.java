package ru.fqw.TestingServis.bot.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.repo.TelegramUserRepo;
import ru.fqw.TestingServis.bot.service.TelegramUserService;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.service.UserService;

@Service
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {

  final TelegramUserRepo telegramUserRepo;
  final UserService userService;

  public TelegramUser saveTelegramUser(TelegramUser telegramUser) {
    return telegramUserRepo.save(telegramUser);
  }

  public List<TelegramUser> getTelegramUserByAuthenticationUser() {
    return telegramUserRepo.findByuserSetInvited(userService.getAuthenticationUser());
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
