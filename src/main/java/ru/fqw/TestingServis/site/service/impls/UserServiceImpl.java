package ru.fqw.TestingServis.site.service.impls;

import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.UserRepository;
import ru.fqw.TestingServis.site.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  UserRepository userRepository;

  private BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  @Transactional
  public User saveUser(User user) {
    if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
      user.setPassword(encoder().encode(user.getPassword()));
      user.setActivite(true);
      return userRepository.save(user);
    }
    return null;
  }

  @Override
  @Transactional()
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
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("User not found")
    );
  }

  @Override
  @Transactional
  public boolean containsTelegramUser(User user, TelegramUser telegramUser) {
    Set<TelegramUser> telegramUserSet = user.getTelegramUsers();
    return telegramUserSet.contains(telegramUser);
  }

}