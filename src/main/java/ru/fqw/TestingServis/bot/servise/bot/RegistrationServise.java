package ru.fqw.TestingServis.bot.servise.bot;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.servise.TelegramUserServise;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.servise.UserServise;

import java.util.HashMap;
import java.util.Map;
@AllArgsConstructor
@Service

public class RegistrationServise {
    private Map<Long,TelegramUser> registrationMap = new HashMap<>(); //Ключ - id чата в телеграмме
    UserServise userServise;
    TelegramUserServise telegramUserServise;
    public void registration(Update update, TelegramBot telegramBot){ //Обработка регистрации в телеграмм боте
        Message message = update.getMessage();
        boolean isUserReg = telegramUserServise.telegramUserExistsByChatId(message.getChatId());
        boolean isCommandReg = message.getText().equals("/reg");
        boolean isUserNowReg = registrationMap.get(message.getChatId()) != null;
        if (!isUserReg && !isCommandReg && !isUserNowReg){
            telegramBot.sendMessege(message.getChatId(),"Для продолжения работы вам необходимо зарегистрироваться. (/reg)");

        }
        else if (isCommandReg && !isUserNowReg){
            if (telegramUserServise.telegramUserExistsByChatId(message.getChatId())){
                telegramBot.sendMessege(message.getChatId(),"Вы уже зарегестрированны.");
                return;
            }
            registrationMap.put(message.getChatId(), new TelegramUser());
            System.out.println(registrationMap.get(message.getChatId()) == null);
            telegramBot.sendMessege(message.getChatId(),"Введите ваше имя");
        }
        else if (isUserNowReg){
            TelegramUser telegramUser = registrationMap.get(message.getChatId());
            if (telegramUser.getName() == null){
                telegramUser.setName(message.getText());
                telegramBot.sendMessege(message.getChatId(),"Введите вашу фамилию");
            }
            else if (telegramUser.getSurname() == null){
                telegramUser.setSurname(message.getText());
                telegramBot.sendMessege(message.getChatId(),"Введите почту человека, который будет иметь возможность отправлять вам тесты");
            }
            else if (telegramUser.getUserSetInvited().isEmpty()){
                try {
                    User user = userServise.getUserByEmail(message.getText());
                    telegramUser.getUserSetInvited().add(user);
                    telegramUser.setChatId(message.getChatId());
                    telegramUserServise.saveTelegramUser(telegramUser);
                    telegramBot.sendMessege(message.getChatId(),telegramUser.getName() + ", вы успешно зарегестрировались.");
                    registrationMap.remove(message.getChatId());
                }
                catch (ResourceNotFoundException e){
                    telegramBot.sendMessege(message.getChatId(),"Пользователь с указанной почтой не найден. Повторите ввод");
                }
            }

        }
    }
}
