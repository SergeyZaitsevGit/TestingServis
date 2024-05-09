package ru.fqw.TestingServis.bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.fqw.TestingServis.bot.repo.InvitedAddRepo;
import ru.fqw.TestingServis.bot.repo.InvitedRemoveRepo;
import ru.fqw.TestingServis.bot.repo.RegistrationRepo;
import ru.fqw.TestingServis.bot.repo.TestingRepo;
import ru.fqw.TestingServis.bot.service.BotMenuService;
import ru.fqw.TestingServis.bot.service.TelegramUserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotMenuServiceImpl implements BotMenuService {

    private final  RegistrationRepo registrationRepo;
    private final InvitedAddRepo invitedAddRepo;
    private final InvitedRemoveRepo invitedRemoveRepo;
    private final TestingRepo testingRepo;
    private final TelegramUserService telegramUserService;

    private static final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    @Override
    public void setMenu(SendMessage sendMessage) {
        initMenu();
        List<KeyboardRow> keyboard = new ArrayList<>();

        if (checkUserProcess(Long.parseLong(sendMessage.getChatId()))) cancel(keyboard);
        if (!telegramUserService.telegramUserExistsByChatId(Long.parseLong(sendMessage.getChatId()))) reg(keyboard);

        setMenuForMessage(sendMessage, keyboard);
    }

    private void cancel(List<KeyboardRow> keyboard){
        KeyboardRow row = new KeyboardRow();
        KeyboardButton cancelButton = new KeyboardButton("/cancel");
        row.add(cancelButton);
        keyboard.add(row);
    }

    private void reg(List<KeyboardRow> keyboard){
        KeyboardRow row = new KeyboardRow();
        KeyboardButton regButton = new KeyboardButton("/reg");
        row.add(regButton);
        keyboard.add(row);
    }

    private boolean checkUserProcess(Long chatId){
        return testingRepo.isUserHaveTest(chatId)
                || invitedAddRepo.isProcessInvited(chatId)
                || invitedRemoveRepo.isProcessInvited(chatId)
                || registrationRepo.isUserPassesRegistration(chatId);
    }

    private void initMenu(){
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
    }

    private void setMenuForMessage(SendMessage sendMessage, List<KeyboardRow> keyboard){
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }


}
