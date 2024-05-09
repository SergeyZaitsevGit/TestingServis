package ru.fqw.TestingServis.bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CancelService {
    void cancel(Update update);
}
