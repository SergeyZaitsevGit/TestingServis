package ru.fqw.TestingServis.bot.repo;

import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.user.User;

import java.util.List;
import java.util.Optional;

public interface TelegramUserRepo extends CrudRepository<TelegramUser, Long> {
    List<TelegramUser> findByuserSetInvited(User user);
    boolean existsByChatId(long chatId);
    Optional<TelegramUser> getTelegramUserByChatId(long chatId);
}
