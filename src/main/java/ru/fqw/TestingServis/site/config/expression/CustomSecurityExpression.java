package ru.fqw.TestingServis.site.config.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.service.TelegramUserService;
import ru.fqw.TestingServis.site.config.UserDetailsImpl;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.service.*;

import java.util.List;


@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

  final UserService userService;
  final QuestionService questionService;
  final TypeService typeService;
  final TelegramUserService telegramUserService;
  final TestService testServis;
  final AnswerService answerService;

  public boolean canAccessQuestion(
      long questionId) { //Проверка является ли пользователь владельцом вопроса, с которым хочет взаимодействовать
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    return questionService.getQuestionById(questionId).getCreator().getEmail()
        .equals(user.getUsername());
  }

  public boolean canAccessAnswer(
      long answerId) { //Проверка является ли пользователь владельцом вопроса, с которым хочет взаимодействовать
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    long questionId = answerService.getAnswerById(answerId).getQuestion().getId();
    return canAccessQuestion(questionId);
  }

  public boolean canAccessType(
      long typeId) { //Проверка является ли пользователь владельцом типа  вопроса, с которым хочет взаимодействовать
      if (typeId == -1) {
          return true;
      }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    return typeService.getTypeById(typeId).getCreator().getEmail().equals(user.getUsername());
  }

  public boolean canAccessTest(
      long testId) { //Проверка является ли пользователь владельцом теста, с которым хочет взаимодействовать
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    return testServis.getTestById(testId).getCreator().getEmail().equals(user.getUsername());
  }

  public boolean canAccessTelegramUser(
      List<Long> chatIds) { //Проверка может ли пользователь взаимодействовать с телеграмм пользователеми
    User user = userService.getAuthenticationUser();
    for (long chatId : chatIds) {
        if (!userService.containsTelegramUser(user,
            telegramUserService.getTelegramUserByChatId(chatId))) {
            return false;
        }
    }
    return true;
  }
}
