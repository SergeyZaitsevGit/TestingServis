package ru.fqw.TestingServis.bot.servise.bot;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.config.BotConfig;

@Service
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    BotConfig botConfig;
    TelegramTestingServise telegramTestingServise;
    RegistrationServise registrationServise;

    @Override
    public void onUpdateReceived(Update update) {

        registrationServise.registration(update,this);
        telegramTestingServise.testing(update);
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
