package ru.fqw.TestingServis.bot.repo;

import org.springframework.stereotype.Component;

public interface InvitedRemoveRepo {
    void save(Long chatId);

    void delete(Long chatId);

    boolean isProcessInvited(Long chatId);
}
