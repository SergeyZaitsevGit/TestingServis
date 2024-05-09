package ru.fqw.TestingServis.bot.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.fqw.TestingServis.bot.config.BotConfig;
import ru.fqw.TestingServis.bot.service.*;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

  private final BotConfig botConfig;
  private final TelegramTestingService telegramTestingService;
  private final RegistrationService registrationServise;
  private final InvitedService invitedService;
  private final BotMenuService botMenuService;
  private final CancelService cancelService;

  @Override
  public void onUpdateReceived(Update update) {
    logUserMessege(update);
    cancelService.cancel(update);
    registrationServise.registration(update);
    telegramTestingService.testing(update);
    invitedService.addInvited(update);
    invitedService.removeInvited(update);
  }

  @SneakyThrows
  @Transactional
  public void sendMessege(long chatId, String text) {
    SendMessage message = new SendMessage(String.valueOf(chatId), text);
    botMenuService.setMenu(message);
    execute(message);
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
