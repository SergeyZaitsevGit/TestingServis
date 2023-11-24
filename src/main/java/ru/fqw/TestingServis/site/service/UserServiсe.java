package ru.fqw.TestingServis.site.service;

import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.user.User;

public interface UserServiсe {

  User saveUser(User user);

  User getAuthenticationUser();

  User getUserByEmail(String email);

  boolean containsTelegramUser(User user, TelegramUser telegramUser);

}
