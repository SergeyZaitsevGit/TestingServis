package ru.fqw.TestingServis.bot.repo;

import ru.fqw.TestingServis.site.models.user.User;

public interface InvitedAddRepo {
    void save(Long chatId);
    void delete(Long chatId);

    boolean isProcessInvited(Long chatId);

    User getUserByChat(Long chatId);

    void addUser(User user, Long chatId);
}
