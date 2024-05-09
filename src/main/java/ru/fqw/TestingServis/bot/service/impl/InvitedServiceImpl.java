package ru.fqw.TestingServis.bot.service.impl;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.models.enums.Command;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.bot.repo.InvitedAddRepo;
import ru.fqw.TestingServis.bot.repo.InvitedRemoveRepo;
import ru.fqw.TestingServis.bot.service.InvitedService;
import ru.fqw.TestingServis.bot.service.TelegramUserService;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.service.GroupService;
import ru.fqw.TestingServis.site.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class InvitedServiceImpl implements InvitedService {

    @Autowired
    private  TelegramUserService telegramUserService;

    @Autowired
    private  UserService userService;

    @Autowired
    private InvitedAddRepo invitedAddRepo;

    @Autowired
    private InvitedRemoveRepo invitedRemoveRepo;

    @Autowired
    private GroupService groupService;

    private final TelegramBot telegramBot;

    @Autowired
    public InvitedServiceImpl(
            @Lazy TelegramBot telegramBot) { // используем ленивую подгрузку, чтобы избежать зацикленности
        this.telegramBot = telegramBot;
    }


    /**
     * Метод добавления "доверенного" лица,
     * которое сможет отправлять тесты телеграмм пользователю
     **/
    @Override
    @Transactional(noRollbackFor = {ResourceNotFoundException.class, IndexOutOfBoundsException.class, NumberFormatException.class})
    public void addInvited(Update update) {
        Message message = update.getMessage();
        boolean isUserReg = telegramUserService.telegramUserExistsByChatId(message.getChatId());
        boolean isCommandInvited = message.getText().equals(Command.ADD_INVITED.getCommandText());
        boolean processInvited = invitedAddRepo.isProcessInvited(message.getChatId());

            if (isUserReg && isCommandInvited && !processInvited){
                telegramBot.sendMessege(message.getChatId(),
                        "Введите почту человека, который будет иметь возможность отправлять вам тесты");
                invitedAddRepo.save(message.getChatId());
            }
            else if (isUserReg && processInvited && invitedAddRepo.getUserByChat(message.getChatId()) == null){
                try {
                    TelegramUser telegramUser = telegramUserService.getTelegramUserByChatId(message.getChatId());
                    User user = userService.getUserByEmail(message.getText());

                    if (telegramUser.getUserSetInvited().contains(user)) {
                        telegramBot.sendMessege(message.getChatId(),
                                user.getUsername() + " уже имеет возможность отправлять вам тесты");
                        return;
                    }
                    telegramUser.getUserSetInvited().add(user);
                    telegramUser.setChatId(message.getChatId());
                    telegramUserService.saveTelegramUser(telegramUser);
                    invitedAddRepo.addUser(user, message.getChatId());

                    List<Group> groups = groupService.getByUser(user);
                    String groupOut = IntStream.range(0, groups.size())
                            .mapToObj(i -> i + 1 + "." + groups.get(i).getName())
                            .collect(Collectors.joining("\n"));

                    telegramBot.sendMessege(message.getChatId(),
                            "Введите группу из списка:\n" + groupOut);
                }
                catch (ResourceNotFoundException e){
                    telegramBot.sendMessege(message.getChatId(),
                            "Пользователь с указанной почтой не найден");
                }
            }
            else if (isUserReg && processInvited && (invitedAddRepo.getUserByChat(message.getChatId()) != null)){
                try {
                    TelegramUser telegramUser = telegramUserService.getTelegramUserByChatId(message.getChatId());
                    int groupIndex = Integer.parseInt(message.getText()) - 1;
                    User user = invitedAddRepo.getUserByChat(message.getChatId());
                    Hibernate.initialize(user);
                    List<Group> groups = groupService.getByUser(user);

                    Group group = groups.get(groupIndex);
                    telegramUser.getGroupSet().add(groupService.getById(group.getId()));
                    telegramUserService.saveTelegramUser(telegramUser);

                    telegramBot.sendMessege(message.getChatId(),
                            "Теперь " + user.getUsername() + " может отправлять вам тесты");
                    invitedAddRepo.delete(message.getChatId());

                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    telegramBot.sendMessege(message.getChatId(),
                            "Введите правильный номер группы");
                }
            }
        }

    /**
     * Метод удаления "доверенного" пользователя,
     * которыЙ больше не сможет отправлять тесты телеграмм-пользователю
     **/
    @Override
    @Transactional
    public void removeInvited(Update update) {
        Message message = update.getMessage();
        boolean isUserReg = telegramUserService.telegramUserExistsByChatId(message.getChatId());
        boolean isCommandInvited = message.getText().equals(Command.REMOVE_INVITED.getCommandText());
        boolean processInvited = invitedRemoveRepo.isProcessInvited(message.getChatId());

        try {
            if (isUserReg && isCommandInvited && !processInvited){
                TelegramUser telegramUser = telegramUserService.getTelegramUserByChatId(message.getChatId());
                List<User> userList = new ArrayList<>(telegramUser.getUserSetInvited());
                String invitedList = IntStream.range(0,userList.size())
                        .mapToObj(i -> i + 1 + "." + userList.get(i).getUsername() + " | " + userList.get(i).getEmail())
                        .collect(Collectors.joining("\n"));
                telegramBot.sendMessege(message.getChatId(),
                        "Введите участника, которого хотите удалить\n" + invitedList);
                invitedRemoveRepo.save(message.getChatId());
            }
            else if (isUserReg && processInvited){
                TelegramUser telegramUser = telegramUserService.getTelegramUserByChatId(message.getChatId());
                List<User> userList = new ArrayList<>(telegramUser.getUserSetInvited());
                User user = userList.get(Integer.parseInt(message.getText()) - 1);
                telegramUser.getUserSetInvited().remove(user);
                telegramUserService.saveTelegramUser(telegramUser);
                telegramBot.sendMessege(message.getChatId(),
                        "Теперь " + user.getUsername() + "не может отправлять вам тесты");
                invitedRemoveRepo.delete(message.getChatId());
            }
        }
        catch (IndexOutOfBoundsException | NumberFormatException e){
            telegramBot.sendMessege(message.getChatId(),
                    "Введен неверный номер пользователя");
        }
    }

}
