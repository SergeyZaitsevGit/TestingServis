package ru.fqw.TestingServis.bot.serviсe.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.config.BotConfig;
import ru.fqw.TestingServis.bot.serviсe.RegistrationServiсe;
import ru.fqw.TestingServis.bot.serviсe.TelegramTestingServiсe;


@Service
@AllArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

  BotConfig botConfig;
  TelegramTestingServiсe telegramTestingServiсe;
  RegistrationServiсe registrationServise;

  @Override
  public void onUpdateReceived(Update update) {
    logUserMessege(update);
    registrationServise.registration(update);
    telegramTestingServiсe.testing(update);

  }

  @SneakyThrows
  public void sendMessege(long chatId, String text) {
    execute(new SendMessage(String.valueOf(chatId), text));
    logBotMessege(chatId, text);
  }

  @Override
  public String getBotUsername() {
    return botConfig.getBotName();
  }

  @Override
  public String getBotToken() {
    return botConfig.getBotToken();
  }

  private void logUserMessege(Update update) {
    Chat chat = update.getMessage().getChat();
    long chatId = chat.getId();
    MDC.put("tgUser", String.valueOf(chatId));
    log.info("User has sent messege: " + update.getMessage().getText());
    MDC.clear();
  }

  private void logBotMessege(long chatId, String text) {
    MDC.put("tgUser", String.valueOf(chatId));
    log.info("Bot has sent messege: " + text);
    MDC.clear();
  }
}
