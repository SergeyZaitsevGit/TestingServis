package ru.fqw.TestingServis.site.config.expression;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.servise.TelegramUserServise;
import ru.fqw.TestingServis.site.config.UserDetailsImpl;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.servise.*;

import java.util.List;
import java.util.Set;

@Service("customSecurityExpression")
@AllArgsConstructor
public class CustomSecurityExpression {

  UserServise userServise;
  QuestionServise questionServise;
  TypeServise typeServise;
  TelegramUserServise telegramUserServise;
  TestServise testServis;
  AnswerServise answerServise;

  public boolean canAccessQuestion(
      long questionId) { //Проверка является ли пользователь владельцом вопроса, с которым хочет взаимодействовать
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    return questionServise.getQuestionById(questionId).getCreator().getEmail()
        .equals(user.getUsername());
  }

  public boolean canAccessAnswer(
      long answerId) { //Проверка является ли пользователь владельцом вопроса, с которым хочет взаимодействовать
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    long questionId = answerServise.getAnswerById(answerId).getQuestion().getId();
    return canAccessQuestion(questionId);
  }

  public boolean canAccessType(
      long typeId) { //Проверка является ли пользователь владельцом типа  вопроса, с которым хочет взаимодействовать
      if (typeId == -1) {
          return true;
      }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    return typeServise.getTypeById(typeId).getCreator().getEmail().equals(user.getUsername());
  }

  public boolean canAccessTest(
      long testId) { //Проверка является ли пользователь владельцом теста, с которым хочет взаимодействовать
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
    return testServis.getTestById(testId).getCreator().getEmail().equals(user.getUsername());
  }

  public boolean canAccessTelegramUser(
      List<Long> chatIds) { //Проверка может ли пользователь взаимодействовать с телеграмм пользователеми
    User user = userServise.getAuthenticationUser();
    for (long chatId : chatIds) {
        if (!userServise.containsTelegramUser(user,
            telegramUserServise.getTelegramUserByChatId(chatId))) {
            return false;
        }
    }
    return true;
  }
}
