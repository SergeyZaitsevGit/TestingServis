package ru.fqw.TestingServis.bot.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.user.User;

public interface TelegramUserRepo extends CrudRepository<TelegramUser, Long> {

  List<TelegramUser> findByuserSetInvited(User user);

  boolean existsByChatId(long chatId);

  Optional<TelegramUser> getTelegramUserByChatId(long chatId);
}
