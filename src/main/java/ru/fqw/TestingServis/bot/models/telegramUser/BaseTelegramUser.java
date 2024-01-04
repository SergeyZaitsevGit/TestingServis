package ru.fqw.TestingServis.bot.models.telegramUser;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseTelegramUser {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
  protected Long id;
  protected long tgId;
  protected long chatId;

  protected String name;
  protected String surname;
  protected String AdditionalInformation;

}
