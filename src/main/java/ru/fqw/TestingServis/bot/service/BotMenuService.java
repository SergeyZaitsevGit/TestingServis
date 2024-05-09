package ru.fqw.TestingServis.bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotMenuService {
    void setMenu(SendMessage sendMessage);
}
