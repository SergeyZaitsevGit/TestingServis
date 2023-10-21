package ru.fqw.TestingServis.bot.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.User;

import java.util.HashSet;
import java.util.Set;

@Data
public class TelegramUser {
//    @Id
//    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private long tgId;
    private long chatId;
    private String name;
    private String surname;
    private String AdditionalInformation;
    private Test test;
    private Set<User> userSetInvited = new HashSet<>();
}
