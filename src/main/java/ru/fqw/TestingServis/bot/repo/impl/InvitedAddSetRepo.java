package ru.fqw.TestingServis.bot.repo.impl;

import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.bot.models.Cancellable;
import ru.fqw.TestingServis.bot.repo.InvitedAddRepo;
import ru.fqw.TestingServis.site.models.user.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class InvitedAddSetRepo implements InvitedAddRepo, Cancellable {
    private final Map<Long, User> invitedMap = Collections.synchronizedMap(new HashMap()); //Плохое решение, но нужна поддержка null значений

    @Override
    public void save(Long chatId) {
        invitedMap.put(chatId, null);
    }

    @Override
    public void delete(Long chatId) {
        invitedMap.remove(chatId);
    }

    @Override
    public boolean isProcessInvited(Long chatId) {
        return invitedMap.containsKey(chatId);
    }

    @Override
    public User getUserByChat(Long chatId) {
        return invitedMap.get(chatId);
    }

    @Override
    public void addUser(User user, Long chatId) {
        invitedMap.put(chatId, user);
    }

    @Override
    public void cancel(Long chatId) {
        delete(chatId);
    }
}
