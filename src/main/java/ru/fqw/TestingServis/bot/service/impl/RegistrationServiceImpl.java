package ru.fqw.TestingServis.bot.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.models.enums.Command;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.repo.RegistrationRepo;
import ru.fqw.TestingServis.bot.service.RegistrationService;
import ru.fqw.TestingServis.bot.service.TelegramUserService;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.service.GroupService;
import ru.fqw.TestingServis.site.service.UserService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

  @Autowired
  private UserService userService;
  @Autowired
  private TelegramUserService telegramUserService;
  @Autowired
  private GroupService groupService;
  @Autowired
  private RegistrationRepo registrationRepo; //Хранилище пользователей, которые проходят регистрацию

  @Autowired
  public RegistrationServiceImpl(
      @Lazy TelegramBot telegramBot) { // используем ленивую подгрузку, чтобы избежать зацикленности
    this.telegramBot = telegramBot;
  }

  private final TelegramBot telegramBot;

  @Override
  @Transactional(noRollbackFor = {ResourceNotFoundException.class, IndexOutOfBoundsException.class, NumberFormatException.class})
  public void registration(Update update) { //Обработка регистрации в телеграмм боте
    Message message = update.getMessage();
    boolean isUserReg = telegramUserService.telegramUserExistsByChatId(message.getChatId());
    boolean isCommandReg = message.getText().equals(Command.REGISTRATION.getCommandText());
    boolean isUserNowReg = registrationRepo.isUserPassesRegistration(message.getChatId());

    if (!isUserReg && !isCommandReg && !isUserNowReg) {
      telegramBot.sendMessege(message.getChatId(),
          "Для продолжения работы вам необходимо зарегистрироваться. (/reg)");

    } else if (isCommandReg && !isUserNowReg) {
      if (telegramUserService.telegramUserExistsByChatId(message.getChatId())) {
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
          User user = userService.getUserByEmail(message.getText());
          telegramUser.getUserSetInvited().add(user);
          telegramUser.setChatId(message.getChatId());
          List<Group> groups = groupService.getByUser(user);
          String groupOut = IntStream.range(0, groups.size())
              .mapToObj(i -> i + 1 + "." + groups.get(i).getName())
              .collect(Collectors.joining("\n"));

          telegramBot.sendMessege(message.getChatId(),
              "Введите группу из списка:\n" + groupOut);
        } catch (ResourceNotFoundException e) {
          telegramBot.sendMessege(message.getChatId(),
              "Пользователь с указанной почтой не найден. Повторите ввод");
        }
      } else if (telegramUser.getGroupSet().isEmpty()) {
        int groupIndex = Integer.parseInt(message.getText()) - 1;
        List<Group> groups = groupService.getByUser(
            telegramUser.getUserSetInvited().iterator().next());
        try {
          Group group = groups.get(groupIndex);
          telegramUser.getGroupSet().add(groupService.getById(group.getId()));
          telegramUserService.saveTelegramUser(telegramUser);
          registrationRepo.delite(message.getChatId());
          telegramBot.sendMessege(message.getChatId(), "Вы успешно зарегистрированны.");
        }
        catch (IndexOutOfBoundsException | NumberFormatException e){
          telegramBot.sendMessege(message.getChatId(), "Введите правильный номер группы");
        }
      }
    }
  }
}
