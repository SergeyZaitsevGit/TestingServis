package ru.fqw.TestingServis.site.servise;

import lombok.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.repo.UserRepository;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServise {

  UserRepository userRepository;

  private BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  public User saveUser(User user) {
    if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
      user.setPassword(encoder().encode(user.getPassword()));
      user.setActivite(true);
      return userRepository.save(user);
    }
    return null;
  }


  public User getAuthenticationUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null) {
          throw new AccessDeniedException("Пользователь не авторизован");
      }
    return getUserByEmail(
        authentication.getName()
    );
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("User not found")
    );
  }

  public boolean containsTelegramUser(User user, TelegramUser telegramUser) {
    Set<TelegramUser> telegramUserSet = user.getTelegramUsers();
    return telegramUserSet.contains(telegramUser);
  }

}
