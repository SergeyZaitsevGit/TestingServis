package ru.fqw.TestingServis.bot.service;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.site.models.test.Test;

public interface TelegramTestingService {
  void testing(Update update);
  void startTest(List<Long> tgUsersChatIds, Test test, String title);
}
