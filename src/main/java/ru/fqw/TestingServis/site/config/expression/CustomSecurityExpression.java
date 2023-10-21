package ru.fqw.TestingServis.site.config.expression;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.config.UserDetailsImpl;
import ru.fqw.TestingServis.site.servise.QuestionServise;
import ru.fqw.TestingServis.site.servise.TypeServise;
import ru.fqw.TestingServis.site.servise.UserServise;

@Service("customSecurityExpression")
@AllArgsConstructor
public class CustomSecurityExpression {
    UserServise userServise;
    QuestionServise questionServise;
    TypeServise typeServise;

    public boolean canAccessQuestion(long questionId){ //Проверка является ли пользователь владельцом вопроса, с которым хочет взаимодействовать
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return questionServise.getQuestionById(questionId).getCreator().getEmail().equals(user.getUsername());
    }

    public boolean canAccessType(long typeId){ //Проверка является ли пользователь владельцом типа  вопроса, с которым хочет взаимодействовать
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        return typeServise.getTypeById(typeId).getCreator().getEmail().equals(user.getUsername());
    }
}
