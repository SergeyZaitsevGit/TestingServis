package ru.fqw.TestingServis.site.service.impls;

import lombok.*;
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
import ru.fqw.TestingServis.site.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  UserRepository userRepository;

  private BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public User saveUser(User user) {
    if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
      user.setPassword(encoder().encode(user.getPassword()));
      user.setActivite(true);
      return userRepository.save(user);
    }
    return null;
  }

  @Override
  public User getAuthenticationUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AccessDeniedException("Пользователь не авторизован");
    }
    return getUserByEmail(
        authentication.getName()
    );
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("User not found")
    );
  }

  @Override
  public boolean containsTelegramUser(User user, TelegramUser telegramUser) {
    Set<TelegramUser> telegramUserSet = user.getTelegramUsers();
    return telegramUserSet.contains(telegramUser);
  }

}
