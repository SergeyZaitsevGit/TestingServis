package ru.fqw.TestingServis.bot.servise;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.config.BotConfig;
import ru.fqw.TestingServis.site.models.Test;

@Component
@AllArgsConstructor

public class TelegramBot extends TelegramLongPollingBot {
    BotConfig botConfig;
    TestingServise testingServise;

    @Override
    public void onUpdateReceived(Update update) {
        testingServise.registration(update,this);
    }

    @SneakyThrows
    public void sendMessege(long chatId, String text){
        execute(new SendMessage(String.valueOf(chatId),text));
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
