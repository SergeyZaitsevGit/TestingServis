package ru.fqw.TestingServis.bot.models;

import jakarta.persistence.Id;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import ru.fqw.TestingServis.bot.models.telegramUser.BaseTelegramUser;
import ru.fqw.TestingServis.site.models.test.BaseTest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ResultTest {

  @Id
  private ObjectId id;
  @Field(targetType = FieldType.OBJECT_ID)
  private BaseTest test;
  private BaseTelegramUser telegramUser;
  private List<AnswerFromTelegramUser> answerFromTelegramUserList = new ArrayList<>();
  private int ball;
  private Date timeStart;
  private Date timeEnd;
  private String title;

  public String getFormatedTimeStart() {
    SimpleDateFormat sdf =
        new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm");
    return sdf.format(timeStart);
  }

  public String getFormatedTimeEnd() {
    SimpleDateFormat sdf =
        new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm");
    return sdf.format(timeEnd);
  }

  public String getTimeOnTest() {
    long diffInMinutes = timeEnd.getTime() - timeStart.getTime();
    Duration duration = Duration.ofMillis(diffInMinutes);
    return String.format("%02d:%02d", duration.toHours(), duration.toMinutes());
  }

}
