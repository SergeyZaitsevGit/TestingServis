package ru.fqw.TestingServis.bot.healpers;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

public class TimeUtils {
@Value("utils.time-zone")
private static String zone;

  public static Timestamp getNow(){
        Instant instant = Instant.now();
        return Timestamp.from(instant.atZone(ZoneId.of(zone)).toInstant());
    }
}

