package ru.fqw.TestingServis.bot.healpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

@Component
public class TimeUtils {
@Value("${utils.time-zone}")
private String zone;

  public Timestamp getNow(){
        Instant instant = Instant.now();
        return Timestamp.from(instant.atZone(ZoneId.of(zone)).toInstant());
    }
}

