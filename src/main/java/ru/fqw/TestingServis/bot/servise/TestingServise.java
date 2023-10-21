package ru.fqw.TestingServis.bot.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.models.TelegramUser;
import ru.fqw.TestingServis.bot.models.TestFromTelegramUser;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.servise.UserServise;

import java.util.HashMap;
import java.util.Map;
@Service
@AllArgsConstructor
public class TestingServise {
    public static Map<TelegramUser, TestFromTelegramUser> testMap;
    Map<Long,TelegramUser> registrationMap = new HashMap<>();
    UserServise userServise;

    public void registration(Update update, TelegramBot telegramBot){
        Message message = update.getMessage();

        if (message.getText().equals("/reg") && registrationMap.get(message.getChatId()) == null){
            registrationMap.put(message.getChatId(), new TelegramUser());
            System.out.println(registrationMap.get(message.getChatId()) == null);
            telegramBot.sendMessege(message.getChatId(),"Введите ваше имя");
        }
        else if (registrationMap.get(message.getChatId()) != null){
            TelegramUser telegramUser = registrationMap.get(message.getChatId());
            if (telegramUser.getName() == null){
                telegramUser.setName(message.getText());
                registrationMap.put(message.getChatId(), telegramUser);
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
                    registrationMap.put(message.getChatId(), telegramUser);
                }
                catch (ResourceNotFoundException e){
                    telegramBot.sendMessege(message.getChatId(),"Пользователь с указанной почтой не найден. Повторите ввод");
                }
                System.out.println(registrationMap.get(message.getChatId()));
            }

        }
    }
}
