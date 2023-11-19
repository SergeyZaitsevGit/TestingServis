package ru.fqw.TestingServis.site.servise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.UserRepository;
import ru.fqw.TestingServis.site.servise.UserServise;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiseTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServise userServise;

  @Test
  void getAuthenticationUser_AuthenticatedUser_ReturnsUser() {
    User expectedUser = new User();
    String userEmail = "test@example.com";
    expectedUser.setEmail(userEmail);

    Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEmail);
    SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(expectedUser));
    User actualUser = userServise.getAuthenticationUser();

    assertNotNull(actualUser);
    assertEquals(expectedUser, actualUser);
    assertEquals(actualUser.getEmail(), userEmail);
    verify(userRepository, times(1)).findByEmail("test@example.com");
  }

  @Test
  void getAuthenticationUser_UnauthenticatedUser_ThrowsAccessDeniedException() {
    Authentication authentication = null;

    SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    assertThrows(AccessDeniedException.class, () -> userServise.getAuthenticationUser());
  }

  @Test
  void getAuthenticationUser_UnauthenticatedUser_ThrowsResoursNoFound() {
    User expectedUser = new User();
    String userEmail = "test@example.com";
    expectedUser.setEmail(userEmail);

    Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
    when(authentication.getName()).thenReturn(userEmail);
    SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    assertThrows(ResourceNotFoundException.class, () -> userServise.getAuthenticationUser());
  }

  @Test
  public void testSaveUser_WhenEmailNotExists_ShouldSaveUser() {
    User user = new User();
    String mail = "existing@example.com";
    String password = "pass";
    user.setEmail(mail);
    user.setPassword(password);
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(user)).thenReturn(user);
    User savedUser = userServise.saveUser(user);

    assertEquals(user,savedUser);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void testSaveUser_WhenEmailExists_ShouldNotSaveUser() {
    User user = new User();
    user.setEmail("existing@example.com");
    user.setActivite(true);

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    User savedUser = userServise.saveUser(user);

    assertNull(savedUser);
    verify(userRepository, times(0)).save(user);
  }

  @Test
  public void containsTelegramUser_WhenTgUserContains_ShouldTrue() {
    String tgName = "testTgName";
    TelegramUser telegramUser = new TelegramUser();
    telegramUser.setName(tgName);
    String userName = "testUserName";
    User user = new User();
    user.setUsername(userName);
    user.setTelegramUsers(Stream.of
            (telegramUser, new TelegramUser())
        .collect(Collectors.toCollection(HashSet::new)));

    boolean result = userServise.containsTelegramUser(user, telegramUser);

    assertTrue(result);

  }

  @Test
  public void containsTelegramUser_WhenTgUserContains_ShouldFalse() {
    String tgName = "testTgName";
    TelegramUser telegramUser = new TelegramUser();
    telegramUser.setName(tgName);
    String userName = "testUserName";
    User user = new User();
    user.setUsername(userName);
    user.setTelegramUsers(Stream.of
            (telegramUser, new TelegramUser())
        .collect(Collectors.toCollection(HashSet::new)));
    TelegramUser telegramUserCheckContains = new TelegramUser();
    telegramUserCheckContains.setName("testTgName2.");

    boolean result = userServise.containsTelegramUser(user, telegramUserCheckContains);

    assertFalse(result);
  }

}

