package ru.fqw.TestingServis.bot.models.telegramUser;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.user.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser extends BaseTelegramUser {

  @ManyToMany(fetch = FetchType.LAZY )
  @JoinTable(
      name = "TelegramUserLikeUser",
      joinColumns = @JoinColumn(name = "telegram_user_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  @org.springframework.data.annotation.Transient
  private Set<User> userSetInvited = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "TelegramUserLikeGroup",
      joinColumns = @JoinColumn(name = "telegram_user_id"),
      inverseJoinColumns = @JoinColumn(name = "group_id"))
  @org.springframework.data.annotation.Transient
  private Set<Group> groupSet = new HashSet<>();
}
