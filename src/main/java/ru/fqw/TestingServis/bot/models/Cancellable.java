package ru.fqw.TestingServis.bot.models;

public interface Cancellable {
    void cancel(Long chatId);
}
