package ru.fqw.TestingServis.bot.models.telegramUser;


import jakarta.persistence.*;
import lombok.*;
import ru.fqw.TestingServis.site.models.user.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser extends  BaseTelegramUser{

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "TelegramUserLikeUser",
            joinColumns = @JoinColumn(name = "telegram_user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @org.springframework.data.annotation.Transient
    private Set<User> userSetInvited = new HashSet<>();

}
