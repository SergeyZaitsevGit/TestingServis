package ru.fqw.TestingServis.bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface InvitedService {
    void addInvited(Update update);

    void removeInvited(Update update);
}
