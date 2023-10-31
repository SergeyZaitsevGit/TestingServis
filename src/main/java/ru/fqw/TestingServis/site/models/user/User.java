package ru.fqw.TestingServis.site.models.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fqw.TestingServis.bot.models.telegramUser.TelegramUser;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "usr")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseUser{

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @org.springframework.data.annotation.Transient
    private List<Test> testList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @org.springframework.data.annotation.Transient
    private List<Question> questionList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @org.springframework.data.annotation.Transient
    private List<Type> typeList;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userSetInvited")
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private Set<TelegramUser> telegramUsers = new HashSet<>();


}