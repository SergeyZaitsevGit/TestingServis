package ru.fqw.TestingServis.site.models.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;


@Entity
@Table(name = "usr")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseUser {

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL)
  @org.springframework.data.annotation.Transient
  private List<Test> testList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL)
  @org.springframework.data.annotation.Transient
  private List<Question> questionList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL)
  @org.springframework.data.annotation.Transient
  private List<Type> typeList;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
  @JsonIgnore
  @org.springframework.data.annotation.Transient
  private List<Group> groups;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userSetInvited")
  @JsonIgnore
  @org.springframework.data.annotation.Transient
  private Set<TelegramUser> telegramUsers = new HashSet<>();


}