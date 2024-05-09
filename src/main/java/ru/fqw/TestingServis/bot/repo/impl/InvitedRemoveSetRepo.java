package ru.fqw.TestingServis.bot.repo.impl;

import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.bot.models.Cancellable;
import ru.fqw.TestingServis.bot.repo.InvitedRemoveRepo;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class InvitedRemoveSetRepo implements InvitedRemoveRepo, Cancellable {
    private final Set<Long> invitedSet = new ConcurrentSkipListSet<>();

    @Override
    public void save(Long chatId) {
        invitedSet.add(chatId);
    }

    @Override
    public void delete(Long chatId) {
        invitedSet.remove(chatId);
    }

    @Override
    public boolean isProcessInvited(Long chatId) {
        return invitedSet.contains(chatId);
    }

    @Override
    public void cancel(Long chatId) {
        delete(chatId);
    }
}
