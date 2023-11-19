package ru.fqw.TestingServis.bot.servise.bot;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.repo.RegistrationRepo;
import ru.fqw.TestingServis.bot.servise.TelegramUserServise;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.servise.UserServise;

@AllArgsConstructor
@Service
public class RegistrationServise {

  UserServise userServise;
  TelegramUserServise telegramUserServise;
  RegistrationRepo registrationRepo; //Хранилище пользователей, который проходят регистрацию

  public void registration(Update update,
      TelegramBot telegramBot) { //Обработка регистрации в телеграмм боте
    Message message = update.getMessage();
    boolean isUserReg = telegramUserServise.telegramUserExistsByChatId(message.getChatId());
    boolean isCommandReg = message.getText().equals("/reg");
    boolean isUserNowReg = registrationRepo.isUserpassesRegistration(message.getChatId());
    if (!isUserReg && !isCommandReg && !isUserNowReg) {
      telegramBot.sendMessege(message.getChatId(),
          "Для продолжения работы вам необходимо зарегистрироваться. (/reg)");

    } else if (isCommandReg && !isUserNowReg) {
      if (telegramUserServise.telegramUserExistsByChatId(message.getChatId())) {
        telegramBot.sendMessege(message.getChatId(), "Вы уже зарегестрированны.");
        return;
      }
      registrationRepo.save(message.getChatId(), new TelegramUser());
      telegramBot.sendMessege(message.getChatId(), "Введите ваше имя");
    } else if (isUserNowReg) {
      TelegramUser telegramUser = registrationRepo.get(message.getChatId());
      if (telegramUser.getName() == null) {
        telegramUser.setName(message.getText());
        telegramBot.sendMessege(message.getChatId(), "Введите вашу фамилию");
      } else if (telegramUser.getSurname() == null) {
        telegramUser.setSurname(message.getText());
        telegramBot.sendMessege(message.getChatId(),
            "Введите почту человека, который будет иметь возможность отправлять вам тесты");
      } else if (telegramUser.getUserSetInvited().isEmpty()) {
        try {
          User user = userServise.getUserByEmail(message.getText());
          telegramUser.getUserSetInvited().add(user);
          telegramUser.setChatId(message.getChatId());
          telegramUserServise.saveTelegramUser(telegramUser);
          telegramBot.sendMessege(message.getChatId(),
              telegramUser.getName() + ", вы успешно зарегестрировались.");
          registrationRepo.delite(message.getChatId());
        } catch (ResourceNotFoundException e) {
          telegramBot.sendMessege(message.getChatId(),
              "Пользователь с указанной почтой не найден. Повторите ввод");
        }
      }

    }
  }
}
