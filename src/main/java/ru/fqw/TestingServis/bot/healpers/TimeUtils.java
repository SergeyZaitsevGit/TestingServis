package ru.fqw.TestingServis.bot.healpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TimeUtils {
@Value("${utils.time-zone}")
private String zone;

  public Timestamp getNow(){
      ZoneId zoneId = ZoneId.of(zone);
      ZonedDateTime now = ZonedDateTime.now(zoneId);
      return Timestamp.valueOf(now.toLocalDateTime());
    }
}

